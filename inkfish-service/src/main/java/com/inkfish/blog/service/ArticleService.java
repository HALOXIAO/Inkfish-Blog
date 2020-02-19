package com.inkfish.blog.service;

import com.inkfish.blog.common.exception.DBTransactionalException;
import com.inkfish.blog.mapper.ArticleMapper;
import com.inkfish.blog.mapper.ArticleTagMapper;
import com.inkfish.blog.mapper.ArticleTagRelationMapper;
import com.inkfish.blog.model.pojo.Article;
import com.inkfish.blog.model.pojo.ArticleTag;
import com.inkfish.blog.service.manager.ImageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

    @Transactional(rollbackFor = DBTransactionalException.class)
    public void deleteArticleById(Integer id) throws IOException {
        if (articleMapper.removeById(id)) {
            imageManager.deleteImage(id);
            return;
        }
        DBTransactionalException e = new DBTransactionalException("删除Image失败");
        log.error(e.getMessage());
        throw e;
    }


}
