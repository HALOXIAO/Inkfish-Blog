package com.inkfish.blog.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.model.pojo.Role;
import org.springframework.stereotype.Component;

/**
 * @author HALOXIAO
 **/

@Component
public class RoleMapper extends ServiceImpl<RoleDao, Role> {

/*    @Autowired
    RoleMapperInterface roleMapperInterface;

    public boolean addRole(Role role) {
        return save(role);
    }

    public String searchIdWithRoleName(String name) {
        return roleMapperInterface.selectOne(
                new QueryWrapper<Role>().select("id").eq("role", name)
        ).getRole();
    }*/

}
