package com.inkfish.blog.web;

import com.inkfish.blog.mapper.ArticleCommentMapper;
import com.inkfish.blog.service.ArticleService;
import com.inkfish.blog.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;


@SpringBootTest
@Component
class InkfishWebApplicationTests {


    @Autowired
    ArticleService articleService;

    @Autowired
    CommentService commentService;

    @Test
    void contextLoads() throws IOException {
       System.out.println( commentService.getCommentWithPage(-1,1,1));
    }
}

