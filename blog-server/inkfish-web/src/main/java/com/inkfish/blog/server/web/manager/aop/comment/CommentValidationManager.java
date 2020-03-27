package com.inkfish.blog.server.web.manager.aop.comment;

import com.alibaba.fastjson.JSON;
import com.inkfish.blog.server.common.ARTICLE_STATUS_CODE;
import com.inkfish.blog.server.common.REDIS_ARTICLE_CACHE_NAMESPACE;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.model.pojo.ArticleComment;
import com.inkfish.blog.server.service.ArticleService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author HALOXIAO
 **/
@Order(1)
@Aspect
@Component
public class CommentValidationManager {
    private  StringRedisTemplate stringRedisTemplate;
    private  ArticleService articleService;

    @Autowired
    public CommentValidationManager(StringRedisTemplate stringRedisTemplate, ArticleService articleService) {
        this.articleService = articleService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Before(value = "execution(* com.inkfish.blog.server.web.controller.CommentController.pushComment())&&args(articleComment)")
    public void articleStatusCheck(ArticleComment articleComment) throws IOException {
        Integer enableComment = (Integer) stringRedisTemplate.opsForHash().get(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_COMMENT_STATUS_INFORMATION.getValue(), articleComment.getArticleId());

        if (enableComment == null) {
            enableComment = articleService.getCommentStatus(articleComment.getArticleId());
        }
        if (enableComment != ARTICLE_STATUS_CODE.ARTICLE_COMMENT_ENABLE.getValue()) {
            HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
            try (PrintWriter printWriter = response.getWriter()) {
                ResultBean<Boolean> bean = new ResultBean<>("target wrong", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
                printWriter.write(JSON.toJSON(bean).toString());
            }
        }


    }
}


