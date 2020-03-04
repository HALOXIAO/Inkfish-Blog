package com.inkfish.blog.server.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.server.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author HALOXIAO
 **/
@Repository
public class UserMapper extends ServiceImpl<UserDao,User> {

    @Autowired
    RoleUserMapper roleUserMapper;

    @Override
    public UserDao getBaseMapper() {
        return super.getBaseMapper();
    }

    public boolean addUser(User user) {
        return save(user);
    }


    public List<String> searchRolenameWithUsername(String username) {

        return getBaseMapper().searchRolenameWithUsername(username);
    }
    public List<String> searchRolenameWithEmail(String email){
        return getBaseMapper().searchRolenameWithEmail(email);
    }


    public User searchUserPasswordWithUsername(String username) {
        return getOne(new QueryWrapper<User>().select("password").eq("username", username));

    }

    public User searchUserPasswordWithEmail(String email){
        return getOne(new QueryWrapper<User>().select("password").eq("email",email));
    }



}
