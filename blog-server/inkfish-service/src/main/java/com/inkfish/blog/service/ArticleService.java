package com.inkfish.blog.service;

import com.inkfish.blog.common.exception.DBTransactionalException;
import com.inkfish.blog.mapper.ArticleMapper;
import com.inkfish.blog.mapper.ArticleTagMapper;
import com.inkfish.blog.mapper.ArticleTagRelationMapper;
import com.inkfish.blog.mapper.convert.ArticleToArticleOverviewVO;
import com.inkfish.blog.model.dto.TagAndArticleDTO;
import com.inkfish.blog.model.pojo.Article;
import com.inkfish.blog.model.pojo.ArticleTag;
import com.inkfish.blog.model.vo.ArticleOverviewVO;
import com.inkfish.blog.service.manager.ImageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    ImageManager imageManager;

    public boolean addArticle(Article article) {
        return articleMapper.save(article);
    }

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

    @Transactional(rollbackFor = {DBTransactionalException.class,IOException.class})
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
        List<TagAndArticleDTO> tagAndArticleDTOList = articleTagMapper.getBaseMapper().getTagAndArticleDTO(articleId);
        Collections.sort(tagAndArticleDTOList);
        TagAndArticleDTO dto = new TagAndArticleDTO();
        Collections.sort(tagAndArticleDTOList);
        List<ArticleOverviewVO> articleOverviewVOList = ArticleToArticleOverviewVO.INSTANCE.toArticleOverviewVOList(articles);
        articleOverviewVOList.parallelStream().forEach(p -> {
            dto.setArticleId(p.getId());
            int index = Collections.<TagAndArticleDTO>binarySearch(tagAndArticleDTOList, dto);
            int base = index;
            List<String> tags = new LinkedList<>();
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


}
