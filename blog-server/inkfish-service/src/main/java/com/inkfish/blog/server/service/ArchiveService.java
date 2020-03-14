package com.inkfish.blog.server.service;

/**
 * @author HALOXIAO
 **/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 用于信息定时归档
 * */
@Service
public class ArchiveService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;


}
