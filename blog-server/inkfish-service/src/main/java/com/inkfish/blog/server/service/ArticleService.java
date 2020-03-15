package com.inkfish.blog.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.server.common.REDIS_NAMESPACE;
import com.inkfish.blog.server.common.exception.DBTransactionalException;
import com.inkfish.blog.server.mapper.ArticleMapper;
import com.inkfish.blog.server.mapper.ArticleTagMapper;
import com.inkfish.blog.server.mapper.ArticleTagRelationMapper;
import com.inkfish.blog.server.mapper.convert.ArticleToArticleOverviewVO;
import com.inkfish.blog.server.model.dto.TagAndArticleDTO;
import com.inkfish.blog.server.model.pojo.Article;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import com.inkfish.blog.server.model.vo.ArticleOverviewVO;
import com.inkfish.blog.server.service.manager.ImageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//TODO 当文章标题重复时所保存的照片的Bug

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
    ImageManager imageManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public Article getArticle(Integer id) {

        return articleMapper.getOne(new QueryWrapper<Article>().select("title,overview,enable_comment,category_id,status," +
                "content,create_time,update_time").eq("id", id));
    }


    public boolean addArticle(Article article) {
        return articleMapper.save(article);
    }

    /**
     * 当有tags时的添加文章
     */
    @Transactional(rollbackFor = DBTransactionalException.class)
    public boolean addArticleWithTags(Article article, List<ArticleTag> tags) {
        if (articleMapper.save(article)) {
            Integer id = article.getId();
            if (articleTagRelationMapper.getBaseMapper().addArticleTagRelation(id, tags)) {
                return true;
            }
        }

        DBTransactionalException e = new DBTransactionalException("add Article error");
        log.error(e.getMessage());
        throw e;
    }

    /**
     * 根据文章的唯一id进行删除
     */
    @Transactional(rollbackFor = {DBTransactionalException.class, IOException.class})
    public void deleteArticleById(Integer id) throws IOException {
        if (articleMapper.removeById(id)) {
            imageManager.deleteImage(id);
            return;
        }
        DBTransactionalException e = new DBTransactionalException("删除Image失败");
        log.error(e.getMessage());
        throw e;
    }

    //TODO need to check
    public List<ArticleOverviewVO> getArticleOverviewPage(Integer page, Integer size) {
        List<Article> articles = articleMapper.getBaseMapper().getArticleOverview(page, size);
        List<Integer> articleId = new ArrayList<>();
        articles.parallelStream().forEach(p -> {
            articleId.add(p.getId());
        });
//        获取articleId与TagName的映射
        List<TagAndArticleDTO> tagAndArticleDTOList = articleTagMapper.getBaseMapper().getTagAndArticleDTO(articleId);
        TagAndArticleDTO dto = new TagAndArticleDTO();
        Collections.sort(tagAndArticleDTOList);
        List<ArticleOverviewVO> articleOverviewVOList = ArticleToArticleOverviewVO.INSTANCE.toArticleOverviewVOList(articles);
//        为每一个ArticleOverview添加tags
        articleOverviewVOList.parallelStream().forEach(p -> {
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

        return addLikesAndWatch(articleOverviewVOList);
    }

    private List<ArticleOverviewVO> addLikesAndWatch(List<ArticleOverviewVO> list) {

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
        for (int i = 0; i < list.size(); i++) {
            Double like = (Double) result.get(i);
            Double view = (Double) result.get(i + 1);
            list.get(i).setLikes(like.intValue());
            list.get(i).setViews(view.intValue());
        }
        return list;
    }

}
