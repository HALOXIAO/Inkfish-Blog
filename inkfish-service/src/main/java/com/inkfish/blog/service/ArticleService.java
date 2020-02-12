package com.inkfish.blog.service;

import com.inkfish.blog.mapper.ArticleMapper;
import com.inkfish.blog.mapper.ArticleTagRelationMapper;
import com.inkfish.blog.model.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author HALOXIAO
 **/
@Service
public class ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleTagRelationMapper articleTagRelationMapper;

    public boolean addArticle(Article article) {
        article.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));

        return articleMapper.save(article);
    }




}
