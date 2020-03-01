package com.inkfish.blog.web;

import com.inkfish.blog.common.REDIS_NAMESPACE;
import com.inkfish.blog.mapper.RoleDao;
import com.inkfish.blog.service.manager.ViewingStatisticsManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


@SpringBootTest
@Component
class InkfishWebApplicationTests {

    @Autowired
    RoleDao roleDao;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ViewingStatisticsManager viewingStatisticsManager;

    private void freshVoteMap(ConcurrentHashMap<AtomicInteger, AtomicLong> vote) {
        RedisSerializer redisSerializer = redisTemplate.getKeySerializer();

        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
                vote.forEach((atomicInteger, atomicLong) -> {
                    AtomicLong value = vote.remove(atomicInteger);
                    stringRedisConn.hIncrBy(REDIS_NAMESPACE.ARTICLE_INFORMATION_WATCH.getValue(), String.valueOf(atomicInteger.get()), value.get());
//                        redisTemplate.opsForHash().increment(REDIS_NAMESPACE.ARTICLE_INFORMATION_VOTE.getValue(), atomicInteger.get(), atomicLong.get());
                });
                return null;
            }
        });
    }

    @Test
    void contextLoads() throws IOException {
        ConcurrentHashMap<AtomicInteger, AtomicLong> vote = new ConcurrentHashMap<>();
        vote.put(new AtomicInteger(1), new AtomicLong(2));
        freshVoteMap(vote);
    }
}

