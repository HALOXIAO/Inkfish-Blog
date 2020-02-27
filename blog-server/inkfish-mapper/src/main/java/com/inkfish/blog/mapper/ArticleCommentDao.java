package com.inkfish.blog.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.model.pojo.ArticleComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface ArticleCommentDao extends BaseMapper<ArticleComment> {
    @Select("SELECT id,username,user_id,comment_body,comment_create_time FROM  article_comment WHERE article_id=#{articleId} LIMIT #{page},#{size}")
    List<ArticleComment> getCommentPageWithArticle(Integer articleId, Integer page, Integer size);

}
