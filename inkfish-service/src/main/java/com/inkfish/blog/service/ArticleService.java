package com.inkfish.blog.service;

import com.inkfish.blog.common.exception.DBTransactionalException;
import com.inkfish.blog.mapper.ArticleMapper;
import com.inkfish.blog.mapper.ArticleTagMapper;
import com.inkfish.blog.mapper.ArticleTagRelationMapper;
import com.inkfish.blog.model.pojo.Article;
import com.inkfish.blog.model.pojo.ArticleTag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author HALOXIAO
 **/
@Service
@Slf4j
public class ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleTagRelationMapper articleTagRelationMapper;

    @Autowired
    ArticleTagMapper articleTagMapper;

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






}
