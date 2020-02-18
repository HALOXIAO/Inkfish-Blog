package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.model.pojo.Article;
import com.inkfish.blog.model.pojo.ArticleAndTagRelation;
import com.inkfish.blog.model.pojo.ArticleTag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author HALOXIAO
 **/

@Mapper
public interface ArticleTagRelationDao extends BaseMapper<ArticleAndTagRelation> {
    @InsertProvider(type =ArticleTagRelationMapper.class,method = "addArticleTagRelationProvider")
    public boolean addArticleTagRelation(Integer articleId, List<ArticleTag>tagNames);
}
