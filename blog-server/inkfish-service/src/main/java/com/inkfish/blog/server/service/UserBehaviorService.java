package com.inkfish.blog.server.service;

import com.inkfish.blog.server.common.REDIS_CACHE_NAMESPACE;
import com.inkfish.blog.server.common.REDIS_NAMESPACE;
import com.inkfish.blog.server.common.VOTE_LIKES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public Map<VOTE_LIKES, Integer> getArticleLikesAndViewsById(Integer id) {
        List<Object> result = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.zScore(REDIS_NAMESPACE.ARTICLE_INFORMATION_WATCH.getValue().getBytes(), id.toString().getBytes());
                connection.zScore(REDIS_NAMESPACE.ARTICLE_INFORMATION_LIKE.getValue().getBytes(), id.toString().getBytes());
                return null;
            }
        });
        Double watch = (Double) result.get(0);
        Double likes = (Double) result.get(1);
        Map<VOTE_LIKES, Integer> map = new HashMap<>(4);
        map.put(VOTE_LIKES.WATCH, watch.intValue());
        map.put(VOTE_LIKES.VOTE, likes.intValue());
        return map;
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
