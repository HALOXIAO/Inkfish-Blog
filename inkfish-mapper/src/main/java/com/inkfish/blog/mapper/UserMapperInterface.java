package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.model.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface UserMapperInterface extends BaseMapper<User> {
}
