package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.model.pojo.Role;
import com.inkfish.blog.model.pojo.RoleUser;
import com.inkfish.blog.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HALOXIAO
 **/
@Component
public class RoleUserMapper extends ServiceImpl<RoleUserDao, RoleUser> {


    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    public boolean addUserRole(String username, String rolename) {
        RoleUser roleUser = new RoleUser();
        User user = userDao.selectOne(new QueryWrapper<User>().select("id").
                eq("username", username));
        Role role = roleDao.selectOne(new QueryWrapper<Role>().select("id").
                eq("role", rolename));
        if (null != user && null != role) {
            roleUser.setRoleId(role.getId());
            roleUser.setUserId(user.getId());
            return save(roleUser);
        }
        return false;
    }


}