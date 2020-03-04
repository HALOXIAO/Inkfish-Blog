package com.inkfish.blog.server.mapper.convert;

import com.inkfish.blog.server.model.front.Register;
import com.inkfish.blog.server.model.pojo.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author HALOXIAO
 **/
@Mapper
public interface RegisterToUser {


    RegisterToUser INSTANCE = Mappers.getMapper(RegisterToUser.class);

    @Mappings({
            @Mapping(source = "username",target = "username"),
            @Mapping(source = "email",target = "email"),
            @Mapping(source = "password",target = "password")
    })
    User from(Register register);

}
