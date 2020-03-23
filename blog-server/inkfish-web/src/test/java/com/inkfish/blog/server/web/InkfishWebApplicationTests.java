package com.inkfish.blog.server.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import com.inkfish.blog.server.service.ArticleTagService;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


@SpringBootTest
@Component
class InkfishWebApplicationTests {

    @Autowired
    HttpSession session;

    @Autowired
    ArticleTagService articleTagService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Test
    void contextLoads() throws JsonProcessingException, IOException {
    }


}
class a{
    private Integer x;

    public void setX(Integer x) {
        this.x = x;
    }
}

class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int CACHE_SIZE;

    public LRUCache(int size) {
        super((int) Math.ceil(size / 0.75f) + 1, 0.75f, true);
        CACHE_SIZE = size;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > CACHE_SIZE;
    }

}
