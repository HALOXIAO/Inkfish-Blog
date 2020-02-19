package com.inkfish.blog.web;

import com.inkfish.blog.mapper.ArticleTagRelationMapper;
import com.inkfish.blog.service.ArticleTagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@SpringBootTest
@Component
class InkfishWebApplicationTests {


    @Autowired
    ArticleTagService service;

    @Autowired
    ArticleTagRelationMapper mapper;

    //        -123678913-1233241
    @Test
    void contextLoads() throws IOException {
        Path path = Paths.get("../../../hahaha");
        System.out.println("-123678913-1233241".indexOf("-"));
        System.out.println(         "-123678913-1233241".lastIndexOf("-"));
        System.out.println("-123678913-1233241".substring(0,10));
    }
}