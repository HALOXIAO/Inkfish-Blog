package com.inkfish.blog.service.manager;

import com.inkfish.blog.common.REDIS_NAMESPACE;
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

    @AfterReturning("execution()")
    public void addLike(Integer id) {

        String username = (String) httpSession.getAttribute("name");

        if (stringRedisTemplate.opsForSet().isMember(String.valueOf(id), username)) {
            return;
        }
        stringRedisTemplate.executePipelined(
                new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
                        StringRedisConnection stringRedisConnection = (StringRedisConnection) connection;
                        stringRedisConnection.sAdd(REDIS_NAMESPACE.ARTICLE_INFORMATION_ALREADY_LIKE_NAMESPACE + String.valueOf(id), username);
                        stringRedisConnection.hIncrBy(REDIS_NAMESPACE.ARTICLE_INFORMATION_LIKE.getValue(), String.valueOf(id), 1);

                        return null;
                    }
                }
        );
    }


}
