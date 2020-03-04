package com.inkfish.blog.server.service.manager;

import com.inkfish.blog.server.common.REDIS_NAMESPACE;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @author HALOXIAO
 **/
@Service
public class LikesStatisticsManager {

    private StringRedisTemplate stringRedisTemplate;

    private HttpSession httpSession;

    @Autowired
    public LikesStatisticsManager(HttpSession httpSession, StringRedisTemplate stringRedisTemplate) {
        this.httpSession = httpSession;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @AfterReturning("execution(*com.inkfish.blog.web.controller.ArticleController.articleLike(Integer))&&args(id)")
    public void addLike(Integer id) {
        String username = (String) httpSession.getAttribute("name");
        //判断用户是否已投票
        if (stringRedisTemplate.opsForSet().isMember(String.valueOf(id), username)) {
            return;
        }
        stringRedisTemplate.executePipelined(
                new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;
                        stringRedisConnection.sAdd(REDIS_NAMESPACE.ARTICLE_INFORMATION_ALREADY_LIKE_NAMESPACE.getValue() + String.valueOf(id), username);
                        stringRedisConnection.hIncrBy(REDIS_NAMESPACE.ARTICLE_INFORMATION_LIKE.getValue(), String.valueOf(id), 1);
                        return null;
                    }
                }
        );
    }


}
