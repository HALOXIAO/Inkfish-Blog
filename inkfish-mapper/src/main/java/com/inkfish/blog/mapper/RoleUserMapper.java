package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.model.pojo.Role;
import com.inkfish.blog.model.pojo.RoleUser;
import com.inkfish.blog.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HALOXIAO
 **/
@Component
public class RoleUserMapper {

    @Autowired
    RoleUserMapperInterface roleUserMapperInterface;

    @Autowired
    UserMapperInterface userMapperInterface;

    @Autowired
    RoleMapperInterface roleMapperInterface;

    public boolean addUserRole(String username, String rolename) {
        RoleUser roleUser = new RoleUser();
        User user = userMapperInterface.selectOne(new QueryWrapper<User>().select("id").
                eq("username", username));
        Role role = roleMapperInterface.selectOne(new QueryWrapper<Role>().select("id").
                eq("role", rolename));
        if (null != user && null != role) {
            roleUser.setRoleId(role.getId());
            roleUser.setUserId(user.getId());
            return roleUserMapperInterface.insert(roleUser) == 1;
        }
        return false;
    }

}
