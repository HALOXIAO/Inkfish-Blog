package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.model.pojo.ArticleComment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


/**
 * @author HALOXIAO
 **/
@Repository
public class ArticleCommentMapper extends ServiceImpl<ArticleCommentDao, ArticleComment> {
}
