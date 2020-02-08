package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inkfish.blog.model.pojo.Role;
import com.inkfish.blog.model.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface UserMapperInterface extends BaseMapper<User> {


    @Select("SELECT role FROM role WHERE id=(SELECT `role_id` FROM role_user WHERE user_id=(SELECT `id` FROM `user` WHERE username=#{username}))")
    List<String> searchRolenameWithUsername(@Param("username") String name);

}
