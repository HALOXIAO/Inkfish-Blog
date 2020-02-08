package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.model.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HALOXIAO
 **/

@Component
public class RoleMapper {

    @Autowired
    RoleMapperInterface roleMapperInterface;

    public boolean addRole(Role role) {
        return roleMapperInterface.insert(role) == 1;
    }

    public String searchIdWithRoleName(String name) {
        return roleMapperInterface.selectOne(
                new QueryWrapper<Role>().select("id").eq("role", name)
        ).getRole();
    }

}
