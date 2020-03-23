package com.inkfish.blog.server.web.manager;

import com.alibaba.fastjson.JSON;
import com.inkfish.blog.server.common.REDIS_CACHE_NAMESPACE;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.model.vo.ArticleVO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 防止缓存穿透
 *
 * @author HALOXIAO
 **/
@Component
@Aspect
@Order(1)
public class CachePenetrationManager {

    private final StringRedisTemplate stringRedisTemplate;

    private final Duration ARTICLE_EXPIRE_TIME = Duration.ofMinutes(2);

    public CachePenetrationManager(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.getArticle(Integer))&&args(id)", returning = "bean", argNames = "id,bean")
    public void getArticleCachePenetration(Integer id, ResultBean<ArticleVO> bean) {
        if (RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION.getValue() == bean.getCode()) {
            stringRedisTemplate.opsForValue().set(REDIS_CACHE_NAMESPACE.ARTICLE_CACHE_NAMESPACE.getValue() + id, JSON.toJSON(bean).toString(), ARTICLE_EXPIRE_TIME);
        }
    }


}
