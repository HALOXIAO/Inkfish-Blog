package com.inkfish.blog.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.inkfish.blog.server.common.REDIS_NAMESPACE;
import com.inkfish.blog.server.common.REDIS_TAG_CACHE_NAMESPACE;
import com.inkfish.blog.server.common.exception.DBTransactionalException;
import com.inkfish.blog.server.mapper.ArticleMapper;
import com.inkfish.blog.server.mapper.ArticleTagMapper;
import com.inkfish.blog.server.mapper.ArticleTagRelationMapper;
import com.inkfish.blog.server.mapper.CountMapper;
import com.inkfish.blog.server.mapper.convert.ArticleToArticleOverviewVO;
import com.inkfish.blog.server.model.dto.TagAndArticleDTO;
import com.inkfish.blog.server.model.pojo.Article;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import com.inkfish.blog.server.model.vo.ArticleOverviewVO;
import com.inkfish.blog.server.service.manager.ImageManager;
import com.inkfish.blog.server.service.utils.ArticleServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * @author HALOXIAO
 **/
@Service
@Slf4j
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagRelationMapper articleTagRelationMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ImageManager imageManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CountMapper countMapper;

    @Autowired
    private ArticleServiceUtils articleServiceUtils;


    public void cleanLikesAndWatch(Integer id) {
        stringRedisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.zRem(REDIS_NAMESPACE.ARTICLE_INFORMATION_WATCH.getValue().getBytes(), String.valueOf(id).getBytes());
                connection.zRem(REDIS_NAMESPACE.ARTICLE_INFORMATION_LIKE.getValue().getBytes(), String.valueOf(id).getBytes());
                connection.del(REDIS_NAMESPACE.ARTICLE_INFORMATION_ALREADY_LIKE_PREFIX.getValue().getBytes());
                return null;
            }
        });
    }

    public Article getArticle(Integer id) {

        return articleMapper.getOne(new QueryWrapper<Article>().select("title,overview,enable_comment,status," +
                "content,create_time,update_time").eq("id", id));
    }


    @Transactional(rollbackFor = DBTransactionalException.class)
    public boolean addArticle(Article article) {
        if (!articleMapper.save(article)) {
            try {
                throw new DBTransactionalException("");
            } catch (DBTransactionalException e) {
                log.error(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        countMapper.addArticleCount();
        return true;
    }


    /**
     * 当有tags时的添加文章
     */
    @Transactional(rollbackFor = DBTransactionalException.class)
    public boolean addArticleWithTags(Article article, List<ArticleTag> tags) {
        List<String> result = articleServiceUtils.checkTagsIsMemberInCache(tags);
        if (result.isEmpty()) {
            result = articleServiceUtils.checkTagsIsMemberInDB(tags);
        }
        List<ArticleTag> newTags = Lists.transform(result, new Function<String, ArticleTag>() {
            @Nullable
            @Override
            public ArticleTag apply(@Nullable String input) {
                ArticleTag tag = new ArticleTag();
                tag.setName(input);
                return tag;
            }
        });

        if (articleMapper.save(article) && articleTagMapper.saveBatch(newTags)) {
            Integer id = article.getId();
            if (articleTagRelationMapper.getBaseMapper().addArticleTagRelation(id, tags)) {
                updateExistTag(result);
                return true;
            }
        }
        try {
            throw new DBTransactionalException("add Article error");
        } catch (DBTransactionalException e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
            return false;
        }
    }


    //    TODO 优化：可以开一个线程来单独执行
    protected void updateExistTag(List<String> tagsName) {
        stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                tagsName.forEach(name -> {
                    connection.sAdd(REDIS_TAG_CACHE_NAMESPACE.CACHE_ARTICLE_TAG.getValue().getBytes(), name.getBytes());
                });
                return null;
            }
        });
    }

    /**
     * 根据文章的唯一id进行删除
     */
    @Transactional(rollbackFor = {DBTransactionalException.class, IOException.class})
    public void deleteArticleById(Integer id) throws IOException {
        if (articleMapper.removeById(id)) {
            countMapper.decrArticleCount();
            imageManager.deleteImage(id);
            return;
        }
        DBTransactionalException e = new DBTransactionalException("删除Image失败");
        log.error(e.getMessage());
        throw e;
    }

    public List<ArticleOverviewVO> getArticleOverviewPage(Integer page, Integer size) {
        List<Article> articles = articleMapper.getBaseMapper().getArticleOverview(page, size);
        List<Integer> articleId = new ArrayList<>();
        articles.stream().forEach(p -> {
            articleId.add(p.getId());
        });
//        获取articleId与TagName的映射
        List<TagAndArticleDTO> tagAndArticleDTOList = articleTagMapper.getBaseMapper().getTagAndArticleDTO(articleId);
        Collections.sort(tagAndArticleDTOList);
        List<ArticleOverviewVO> articleOverviewVOList = ArticleToArticleOverviewVO.INSTANCE.toArticleOverviewVOList(articles);
//        为每一个ArticleOverview添加tags
        articleOverviewVOList.stream().forEach(p -> {
            TagAndArticleDTO dto = new TagAndArticleDTO();
            dto.setArticleId(p.getId());
            int index = Collections.binarySearch(tagAndArticleDTOList, dto);
            int base = index;
            List<String> tags = new LinkedList<>();
//            获取排序好的tagAndArticleDTOList里index周围的符合条件的tag
            if (tagAndArticleDTOList.size() != 0) {
                while (base + 1 != tagAndArticleDTOList.size() && tagAndArticleDTOList.get(base + 1) != null && tagAndArticleDTOList.get(base + 1).getArticleId().equals(
                        dto.getArticleId())) {
                    tags.add(tagAndArticleDTOList.get(base + 1).getNames());
                    base++;
                }
                base = index;
                while (-1 != base - 1 && tagAndArticleDTOList.get(base - 1) != null && tagAndArticleDTOList.get(base - 1).getArticleId().equals(
                        dto.getArticleId())) {
                    tags.add(tagAndArticleDTOList.get(base - 1).getNames());
                    base--;
                }
            }
            p.setTags(tags);
        });

        return articleOverviewVOList;
    }

    public List<ArticleOverviewVO> addArticleOverviewVOLikesAndWatchList(List<ArticleOverviewVO> list) {

        List<Object> result = stringRedisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                for (ArticleOverviewVO overview : list) {
                    connection.zScore(REDIS_NAMESPACE.ARTICLE_INFORMATION_LIKE.getValue().getBytes(), String.valueOf(overview.getId()).getBytes());
                    connection.zScore(REDIS_NAMESPACE.ARTICLE_INFORMATION_WATCH.getValue().getBytes(), String.valueOf(overview.getId()).getBytes());
                }
                return null;
            }
        });
        int tag = 0;
        for (int i = 0; i < list.size(); i++) {
            Double like = (Double) result.get(tag);
            Double view = (Double) result.get(tag + 1);
            list.get(i).setLikes(like.intValue());
            list.get(i).setViews(view.intValue());
            tag = i * 2;
        }
        return list;
    }

    public boolean updateArticle(Article article, Integer id) {
        return articleMapper.update(article, new UpdateWrapper<Article>().eq("id", id));
    }

    public Integer getCommentStatus(Integer id) {
        Article article = articleMapper.getOne(new QueryWrapper<Article>().select("status").eq("id", id));
        if (article == null) {
            return -99;
        }
        return article.getStatus();
    }

    public Timestamp getCreateTime(Integer articleId) {
        return articleMapper.getOne(new QueryWrapper<Article>().select("").eq("id", articleId)).getCreateTime();
    }

}
