package com.inkfish.blog.web;

import com.inkfish.blog.mapper.ArticleTagRelationMapper;
import com.inkfish.blog.service.ArticleTagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@SpringBootTest
@Component
class InkfishWebApplicationTests {


    @Autowired
    ArticleTagService service;

    @Autowired
    ArticleTagRelationMapper mapper;


    @Test
    void contextLoads()throws IOException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");//设置日期格式
        String newsNo = LocalDateTime.now().format(fmt);
        System.out.println(newsNo);
    }
}