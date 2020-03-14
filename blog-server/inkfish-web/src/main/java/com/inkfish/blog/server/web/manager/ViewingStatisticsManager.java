package com.inkfish.blog.server.web.manager;

import com.inkfish.blog.server.common.REDIS_NAMESPACE;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author HALOXIAO
 **/
@Aspect
@Service
public class ViewingStatisticsManager {

    private StringRedisTemplate redisTemplate;

    private ConcurrentHashMap<AtomicInteger, AtomicLong> vote;

    private AtomicLong pushTime;

    //second
    private final static long FLUSH_TIME = 180;

    @Autowired
    public ViewingStatisticsManager(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.vote = new ConcurrentHashMap<>();
        pushTime = new AtomicLong(getCurrentUnixTime());
    }

    //无需担心内存问题，100w条数据所占用的内存只要100多MB,如果你有顾虑，可以使用分片来使内存占用达到一个相当低的程度
    @Pointcut("execution(* com.inkfish.blog.server.web.controller.ArticleController.getArticle(Integer))")
    public void addViewsAfterWatching() {
    }

    @AfterReturning("addViewsAfterWatching()")
    public void addViewsAfterWatching(JoinPoint point) {
        Object[] args = point.getArgs();
        if (0 == args.length) {
            return;
        }
        AtomicInteger articleId = new AtomicInteger((Integer) args[0]);
        if (!vote.containsKey(articleId)) {
            vote.put(articleId, new AtomicLong(1));
        } else {
            vote.get(articleId).incrementAndGet();
        }
        if (getCurrentUnixTime() - pushTime.get() > FLUSH_TIME) {
            freshVoteMap(vote);
        }
    }


    /**
     * 将缓存的数值刷入Redis
     */
    private void freshVoteMap(ConcurrentHashMap<AtomicInteger, AtomicLong> vote) {
        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
                vote.forEach((atomicInteger, atomicLong) -> {
                    AtomicLong value = vote.remove(atomicInteger);
                    stringRedisConn.zIncrBy(REDIS_NAMESPACE.ARTICLE_INFORMATION_WATCH.getValue(),value.get(),String.valueOf(atomicInteger.get()));
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
