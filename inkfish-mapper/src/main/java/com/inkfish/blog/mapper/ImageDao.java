package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.model.pojo.Image;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface ImageDao extends BaseMapper<Image> {
}
