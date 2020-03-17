package com.inkfish.blog.server.service;

import com.inkfish.blog.server.common.REDIS_NAMESPACE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author HALOXIAO
 **/
@Service
public class UserBehaviorService {

    private StringRedisTemplate redisTemplate;

    @Autowired
    public UserBehaviorService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Integer getArticleLikes(List<Object> id) {
        List<Object> list = redisTemplate.opsForHash().multiGet(REDIS_NAMESPACE.ARTICLE_INFORMATION_LIKE.getValue(), id);
        List<Integer> lists = new LinkedList<>();
        return null;
    }

    public Integer getArticleViews(Integer id) {
        return (Integer) redisTemplate.opsForHash().get(REDIS_NAMESPACE.ARTICLE_INFORMATION_WATCH.getValue(), id);
    }

    public Integer getArticleLikesById(Integer id) {
        Long likes = redisTemplate.opsForZSet().rank(REDIS_NAMESPACE.ARTICLE_INFORMATION_LIKE.getValue(), String.valueOf(id));
        if (likes != null) {
            return likes.intValue();
        }
        return null;
    }

    public Integer getArticleViewsById(Integer id) {
        Long views = redisTemplate.opsForZSet().rank(REDIS_NAMESPACE.ARTICLE_INFORMATION_WATCH.getValue(), String.valueOf(id));
        if (views != null) {
            return views.intValue();
        }
        return null;

    }

    public void initArticleViewsAndLikes(Integer id) {
        List<Object> result = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.zAdd(REDIS_NAMESPACE.ARTICLE_INFORMATION_LIKE.getValue().getBytes(), 0, id.toString().getBytes());
                connection.zAdd(REDIS_NAMESPACE.ARTICLE_INFORMATION_WATCH.getValue().getBytes(), 0, id.toString().getBytes());
                return null;
            }
        });
    }


}
