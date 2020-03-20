package com.inkfish.blog.server.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.model.vo.ArticleVO;
import com.inkfish.blog.server.service.ArticleTagService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
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
    void contextLoads() throws JsonProcessingException {
        String cache = stringRedisTemplate.opsForValue().get("cache:article:information::14");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(cache);
        JsonNode result = jsonNode.get("data").get("watch");
        System.out.println(result.asText());


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
