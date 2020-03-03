package com.inkfish.blog.server.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inkfish.blog.server.model.pojo.Role;
import org.springframework.stereotype.Repository;

/**
 * @author HALOXIAO
 **/

@Repository
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
