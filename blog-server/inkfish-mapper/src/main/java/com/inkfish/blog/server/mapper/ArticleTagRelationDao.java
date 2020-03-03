package com.inkfish.blog.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.server.model.pojo.ArticleAndTagRelation;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author HALOXIAO
 **/

@Mapper
public interface ArticleTagRelationDao extends BaseMapper<ArticleAndTagRelation> {
    @InsertProvider(type =ArticleTagRelationMapper.class,method = "addArticleTagRelationProvider")
    public boolean addArticleTagRelation(@Param("id") Integer articleId,@Param("tags") List<ArticleTag>tagNames);
}
