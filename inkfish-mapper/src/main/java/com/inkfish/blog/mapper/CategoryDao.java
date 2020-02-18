package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.model.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface CategoryDao extends BaseMapper<Category> {
}
