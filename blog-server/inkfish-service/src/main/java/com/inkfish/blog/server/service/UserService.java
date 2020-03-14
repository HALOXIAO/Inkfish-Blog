package com.inkfish.blog.server.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.server.common.exception.DBTransactionalException;
import com.inkfish.blog.server.mapper.RoleMapper;
import com.inkfish.blog.server.mapper.RoleUserMapper;
import com.inkfish.blog.server.mapper.UserDao;
import com.inkfish.blog.server.mapper.UserMapper;
import com.inkfish.blog.server.model.pojo.User;
import com.inkfish.blog.server.service.manager.EmailManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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

    @Autowired
    EmailManager emailManager;

    @Transactional(rollbackFor = DBTransactionalException.class)
    public boolean addUser(User user, String rolename) {
        if (!userMapper.addUser(user) || !roleUserMapper.addUserRole(user.getUsername(), rolename)) {
            try {
                throw new DBTransactionalException("添加用户发生异常");
            } catch (DBTransactionalException e) {
                log.error(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        return true;
    }

    @Transactional(rollbackFor = DBTransactionalException.class)
    public boolean addUser(User user) {
        String rolename = "ROLE_NORMAL";
        if (!userMapper.addUser(user) || !roleUserMapper.addUserRole(user.getUsername(), rolename)) {
            try {
                throw new DBTransactionalException("添加用户发生异常");
            } catch (DBTransactionalException e) {
                log.error(e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        return true;
    }

    public boolean updatePasswordWithEmail(String email, String password) {
        User user = new User();
        user.setPassword(password);
        return userMapper.update(user, new UpdateWrapper<User>().eq("email", email));
    }

    public List<String> searchRolenameWithUsername(String username) {
        return userMapper.searchRolenameWithUsername(username);
    }

    public List<String> searchRolenameWithEmail(String email) {
        return userMapper.searchRolenameWithEmail(email);
    }

    public String searchUserPasswordWithUsername(String username) {
        User user = userMapper.searchUserPasswordWithUsername(username);
        return user == null ? null : user.getPassword();
    }

    public String searchUserPasswordWithEmail(String email) {
        User user = userMapper.searchUserPasswordWithEmail(email);
        return user == null ? null : user.getPassword();
    }

}