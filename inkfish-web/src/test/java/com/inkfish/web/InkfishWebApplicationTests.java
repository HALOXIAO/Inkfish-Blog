package com.inkfish.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
class InkfishWebApplicationTests {

    @Test
    void contextLoads() {
        HashMap<Integer ,Integer>map  = new HashMap<>();
        Map<String,String>concurrentMap = new ConcurrentHashMap<>();
    }

}
