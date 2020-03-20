package com.inkfish.blog.server.web.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inkfish.blog.server.common.REDIS_CACHE_NAMESPACE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.model.vo.ArticleVO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.checkerframework.checker.units.qual.A;
import org.nustaq.kson.ArgTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * @author HALOXIAO
 **/
@Order(2)
@Component
public class ArticleCacheManager {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public ArticleCacheManager(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Around("execution(* com.inkfish.blog.server.web.controller.ArticleController.getArticle(Integer))&&args(id)")
    public ResultBean<ArticleVO> articleCache(ProceedingJoinPoint pjp, Integer id) throws JsonProcessingException {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        String resultBean = stringRedisTemplate.opsForValue().get(REDIS_CACHE_NAMESPACE.ARTICLE_CACHE_NAMESPACE.getValue());
        ObjectMapper mapper = new ObjectMapper();
        TreeNode node = mapper.readTree(resultBean);
        node.traverse();
        return null;
    }

}
