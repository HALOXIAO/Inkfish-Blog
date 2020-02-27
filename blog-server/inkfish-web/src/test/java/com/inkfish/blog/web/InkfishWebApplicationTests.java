package com.inkfish.blog.web;

import com.inkfish.blog.mapper.ArticleCommentMapper;
import com.inkfish.blog.mapper.RoleDao;
import com.inkfish.blog.model.pojo.Role;
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
    RoleDao roleDao;


    @Test
    void contextLoads() throws IOException {

    }
}

