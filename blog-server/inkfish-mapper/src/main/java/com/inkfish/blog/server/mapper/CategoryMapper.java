package com.inkfish.blog.server.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.server.model.pojo.Category;
import org.springframework.stereotype.Repository;

/**
 * @author HALOXIAO
 **/
@Repository
public class CategoryMapper extends ServiceImpl<CategoryDao, Category> {
}
