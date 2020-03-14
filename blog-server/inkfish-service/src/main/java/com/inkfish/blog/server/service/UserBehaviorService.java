package com.inkfish.blog.server.service;

import com.inkfish.blog.server.common.REDIS_NAMESPACE;
import org.springframework.beans.factory.annotation.Autowired;
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

}
