package com.inkfish.blog.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.server.model.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author HALOXIAO
 **/
@Component
@Mapper
public interface UserDao extends BaseMapper<User> {

    @Select("SELECT role FROM role WHERE EXISTS (SELECT `role_id` FROM role_user WHERE user_id=(SELECT `id` FROM `user` WHERE username=#{username}))")
    List<String> searchRolenameWithUsername(@Param("username") String name);


    @Select("SELECT role FROM role WHERE EXISTS(SELECT `role_id` FROM role_user WHERE user_id=(SELECT `id` FROM `user` WHERE email=#{email}))")
    List<String> searchRolenameWithEmail(@Param("email") String email);

}
