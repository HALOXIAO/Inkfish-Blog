package com.inkfish.blog.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@SpringBootTest
class InkfishWebApplicationTests {


    @Test
    void contextLoads() {
        Map<String, String> map = new ConcurrentHashMap<>();
    }

}
