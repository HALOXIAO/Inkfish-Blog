package com.inkfish.blog.web.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author HALOXIAO
 **/
@Component
public class LikesStatisticsHandler {

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public LikesStatisticsHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }



}
