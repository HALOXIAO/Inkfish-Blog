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

    public boolean addUser(User user){
        return userMapperInterface.insert(user)==1;
    }

    public User searchUserWithName(String name){
        return userMapperInterface.selectOne(
                new QueryWrapper<User>().eq("username",name)
        );
    }

    public List<String>searchRolenameWithUsername(String username){
        return userMapperInterface.searchRolenameWithUsername(username);
    }


}
