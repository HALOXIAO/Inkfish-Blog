package com.inkfish.blog.web;

import com.inkfish.blog.mapper.ArticleTagMapper;
import com.inkfish.blog.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Component
class InkfishWebApplicationTests {


    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleTagMapper tagMapper;

    @Test
    void contextLoads() throws IOException {
    }
}
