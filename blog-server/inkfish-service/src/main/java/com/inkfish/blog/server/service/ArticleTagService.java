package com.inkfish.blog.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.server.mapper.ArticleTagMapper;
import com.inkfish.blog.server.model.pojo.Article;
import com.inkfish.blog.server.model.pojo.ArticleComment;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HALOXIAO
 **/


@Service
public class ArticleTagService {

    @Autowired
    ArticleTagMapper articleTagMapper;

    public boolean addTags(List<ArticleTag> tags) {
        return articleTagMapper.saveBatch(tags);
    }

    public boolean deleteTagWithName(String name) {
        return articleTagMapper.remove(new QueryWrapper<ArticleTag>().eq("name", name));
    }

    public List<ArticleTag> getAllTags() {
        return articleTagMapper.list(new QueryWrapper<ArticleTag>().select("name"));
    }

    public List<Article> getArticle(String tag) {
        return articleTagMapper.getBaseMapper().getArticleOverview(tag);


    }


}
