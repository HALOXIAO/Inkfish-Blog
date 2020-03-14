package com.inkfish.blog.server.web.controller;

import com.inkfish.blog.server.common.REDIS_NAMESPACE;
import com.inkfish.blog.server.common.ResultBean;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author HALOXIAO
 **/
@Api("管理员信息相关")
@RestController
public class AdminPanelController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/admin/information/likes")
    public ResultBean<List<String>> getLikes() {
        TreeSet<String> likes = (TreeSet<String>) stringRedisTemplate.opsForZSet().range(REDIS_NAMESPACE.ARTICLE_INFORMATION_LIKE.getValue(), -10, -1);
        ArrayList<String> list = new ArrayList<>();
        return null;
    }

    @GetMapping("/admin/information/views")
    public ResultBean<List<String>> getViews(){
        TreeSet<String>watch = (TreeSet<String>) stringRedisTemplate.opsForZSet().range(REDIS_NAMESPACE.ARTICLE_INFORMATION_WATCH.getValue(),-10,-1);
        ArrayList<String> list = new ArrayList<>();
        return null;
    }




}
