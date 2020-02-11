package com.inkfish.blog.mapper.convert;

        import com.inkfish.blog.model.front.Register;
        import com.inkfish.blog.model.pojo.User;
        import org.mapstruct.Mapper;
        import org.mapstruct.Mapping;
        import org.mapstruct.Mappings;
        import org.mapstruct.factory.Mappers;
        import org.springframework.stereotype.Component;

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
