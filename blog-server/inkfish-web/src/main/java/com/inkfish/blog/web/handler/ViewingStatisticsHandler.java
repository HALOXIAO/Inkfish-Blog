package com.inkfish.blog.web.handler;

import com.inkfish.blog.common.REDIS_NAMESPACE;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author HALOXIAO
 **/

@Component
public class ViewingStatisticsHandler {

    private StringRedisTemplate redisTemplate;

    private ConcurrentHashMap<AtomicInteger, AtomicLong> vote;

    private AtomicLong pushTime;

    //second
    private final static long FLUSH_TIME = 180;

    @Autowired
    public ViewingStatisticsHandler(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.vote = new ConcurrentHashMap<>();
        pushTime = new AtomicLong(getCurrentUnixTime());
    }

    //TODO Redis Handle
    //无需担心内存问题，100w条数据所占用的内存只要100多MB,如果你有顾虑，可以使用分片来使内存占用达到一个相当低的程度
    @AfterReturning("execution(*com.inkfish.blog.web.controller.ArticleController.getArticle(Integer))&&args(id)")
    public void addLikesAfterWatching(Integer id) {
        AtomicInteger articleId = new AtomicInteger(id);
        if (!vote.containsKey(articleId)) {
            vote.put(articleId, new AtomicLong(0));
        } else {
            vote.get(articleId).incrementAndGet();
        }
        if (getCurrentUnixTime() - pushTime.get() > FLUSH_TIME) {
            freshVoteMap(vote);
        }
    }

    private void freshVoteMap(ConcurrentHashMap<AtomicInteger, AtomicLong> vote) {
        redisTemplate.executePipelined(new RedisCallback<String>() {
            RedisSerializer redisSerializer = redisTemplate.getKeySerializer();

            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                vote.forEach((atomicInteger, atomicLong) -> {
                    AtomicLong value = vote.remove(atomicInteger);
                    connection.hIncrBy(redisSerializer.serialize(REDIS_NAMESPACE.ARTICLE_INFORMATION_WATCH.getValue()), redisSerializer.serialize(atomicInteger.get()), value.get());
//                        redisTemplate.opsForHash().increment(REDIS_NAMESPACE.ARTICLE_INFORMATION_VOTE.getValue(), atomicInteger.get(), atomicLong.get());
                });
                return null;
            }
        });
    }

    private Long getCurrentUnixTime() {
        // Beijing:UTC+8
        ZoneOffset zoneOffset = ZoneOffset.ofHours(8);
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.toEpochSecond(zoneOffset);
    }


}
