package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.model.pojo.Category;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @author HALOXIAO
 **/
@Repository
public class CategoryMapper extends ServiceImpl<CategoryDao, Category> {
}
