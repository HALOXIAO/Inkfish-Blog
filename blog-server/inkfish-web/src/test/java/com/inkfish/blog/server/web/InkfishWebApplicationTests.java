package com.inkfish.blog.server.web;

import ch.qos.logback.core.joran.action.TimestampAction;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.inkfish.blog.server.mapper.convert.ArticleToArticleOverviewVO;
import com.inkfish.blog.server.model.pojo.Article;
import com.inkfish.blog.server.model.pojo.ArticleTag;
import com.inkfish.blog.server.model.vo.ArticleOverviewVO;
import com.inkfish.blog.server.service.ArticleTagService;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    protected final String DATE_PATTERN = "yyyy-MM-dd";


    @Test
    void contextLoads() throws JsonProcessingException, IOException {
        Article article = new Article();
        article.setId(1);
        article.setContent("1");
        Timestamp time = Timestamp.valueOf(LocalDateTime.now());
        article.setCreateTime(time);
        article.setUpdateTime(time);
        article.setStatus(1);
        article.setOverview("1");
        article.setTitle("1");

        ArticleOverviewVO vo = ArticleToArticleOverviewVO.INSTANCE.toArticle(article);
        System.out.println(vo.toString());

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
