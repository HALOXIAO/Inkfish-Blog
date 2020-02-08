package com.inkfish.blog.service;

import com.inkfish.blog.mapper.RoleMapper;
import com.inkfish.blog.mapper.RoleUserMapper;
import com.inkfish.blog.mapper.UserMapper;
import com.inkfish.blog.model.pojo.User;
import com.inkfish.blog.web.exception.DBTransactionalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author HALOXIAO
 **/
@Service
@Slf4j
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    RoleUserMapper roleUserMapper;

    @Transactional(rollbackFor = DBTransactionalException.class)
    public boolean addUser(User user, String rolename) {
        if (!userMapper.addUser(user) || !roleUserMapper.addUserRole(user.getUsername(), rolename)) {
            DBTransactionalException exception = new DBTransactionalException("添加用户发生异常");
            log.error(exception.getMessage());
            throw exception;
        }
        return true;
    }

    public User searchUserWithUsername(String username) {
        return userMapper.searchUserWithName(username);
    }

    public List<String> searchRolenameWithUsername(String username) {
        return userMapper.searchRolenameWithUsername(username);
    }

}