package com.inkfish.blog.service.manager;

import com.google.common.collect.ImmutableList;
import com.inkfish.blog.model.pojo.User;
import com.inkfish.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HALOXIAO
 **/
@Component
public class UserDetailsServiceCustomer implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.searchUserWithUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("not user");
        }
        List<String> roleName = userService.searchRolenameWithUsername(username);
        List<GrantedAuthority> list = new ArrayList<>();
        roleName.forEach(p -> list.add((GrantedAuthority) () -> p));
        return new UserDetailsCustomer(username, user.getPassword(), ImmutableList.copyOf(list));
    }
}
