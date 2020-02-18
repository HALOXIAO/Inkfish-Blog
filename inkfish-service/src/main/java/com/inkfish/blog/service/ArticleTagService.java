package com.inkfish.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.mapper.ArticleTagDao;
import com.inkfish.blog.mapper.ArticleTagMapper;
import com.inkfish.blog.model.pojo.ArticleTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HALOXIAO
 **/


@Service
public class ArticleTagService {

    @Autowired
    ArticleTagMapper articleTagMapper;

    public boolean addTags(List<ArticleTag> tags) {
        return articleTagMapper.saveBatch(tags);
    }

    public boolean deleteTagWithName(String name){
        return articleTagMapper.remove(new QueryWrapper<ArticleTag>().eq("name",name));
    }



}
