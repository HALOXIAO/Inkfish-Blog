package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.model.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface RoleDao extends BaseMapper<Role> {
}
