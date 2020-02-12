package com.inkfish.blog.web;

import com.inkfish.blog.mapper.UserMapper;
import com.inkfish.blog.model.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

@SpringBootTest
@Component
class InkfishWebApplicationTests  {


    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {


        /*        LocalDateTime time = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(time);
        articleMapperInterface.addArticle("helo", true, 1, 1, "asdawd", timestamp);
        System.out.println(articleMapperInterface.getLstId());*/
    }

}