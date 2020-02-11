package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.inkfish.blog.model.pojo.Role;
import com.inkfish.blog.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author HALOXIAO
 **/
@Component
public class UserMapper {

    @Autowired
    UserMapperInterface userMapperInterface;

    public boolean addUser(User user) {
        return userMapperInterface.insert(user) == 1;
    }


    public List<String> searchRolenameWithUsername(String username) {
        return userMapperInterface.searchRolenameWithUsername(username);
    }
    public List<String> searchRolenameWithEmail(String email){
        return userMapperInterface.searchRolenameWithEmail(email);
    }


    public User searchUserPasswordWithUsername(String username) {
        return userMapperInterface.selectOne(new QueryWrapper<User>().select("password").eq("username", username));

    }

    public User searchUserPasswordWithEmail(String email){
        return userMapperInterface.selectOne(new QueryWrapper<User>().select("password").eq("email",email));
    }



}
