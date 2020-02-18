package com.inkfish.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.mapper.CategoryMapper;
import com.inkfish.blog.model.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HALOXIAO
 **/
@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    public boolean addCategory(Category category) {
        return categoryMapper.save(category);
    }

    public Integer searchIdWithName(String name) {
        return categoryMapper.getOne(new QueryWrapper<Category>().select("id").eq("name", name)).getId();
    }

    public boolean deleteCategoryWithName(String name) {
        return categoryMapper.remove(new QueryWrapper<Category>().eq("name", name));
    }

}
