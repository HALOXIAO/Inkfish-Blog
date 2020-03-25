package com.inkfish.blog.server.web.manager.aop.article;

import com.inkfish.blog.server.common.REDIS_ARTICLE_CACHE_NAMESPACE;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.model.front.ArticlePush;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author HALOXIAO
 **/

@Order(2)
@Component
@Aspect
public class ArticleStatusCacheManager {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public ArticleStatusCacheManager(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.publishArticle())&&args(articlePush)", returning = "bean", argNames = "articlePush,bean")
    public void updateArticleStatus(ArticlePush articlePush, ResultBean<Integer> bean) {
        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode()) {
            stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.hSet(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_STATUS_INFORMATION.getValue().getBytes(), bean.getData().toString().getBytes(), articlePush.getStatus().toString().getBytes());
                    connection.hSet(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_COMMENT_STATUS_INFORMATION.getValue().getBytes(), bean.getData().toString().getBytes(), articlePush.getEnableComment().toString().getBytes());
                    return null;
                }
            });
        }
    }

    @AfterReturning(value = "execution(* com.inkfish.blog.server.web.controller.ArticleController.deleteArticle(Integer))&&args(id)", returning = "bean", argNames = "id,bean")
    public void deleteArticleStatus(Integer id, ResultBean<String> bean) {

        if (RESULT_BEAN_STATUS_CODE.SUCCESS.getValue() == bean.getCode()) {
            stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.hDel(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_STATUS_INFORMATION.getValue().getBytes(), id.toString().getBytes());
                    connection.hDel(REDIS_ARTICLE_CACHE_NAMESPACE.CACHE_ARTICLE_COMMENT_STATUS_INFORMATION.getValue().getBytes(), id.toString().getBytes());
                    return null;
                }
            });
        }

    }


}
