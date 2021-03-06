package com.inkfish.blog.server.web.config.customer;

import com.google.common.collect.ImmutableList;
import com.inkfish.blog.server.service.UserService;
import com.inkfish.blog.server.common.util.FieldJudgement;
import com.inkfish.blog.server.web.config.customer.UserDetailsCustomer;
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
    public UserDetails loadUserByUsername(String usernameOrPhoneOrEmail) throws UsernameNotFoundException {
        String password;
        List<String> roleName;
        if (FieldJudgement.isEmail(usernameOrPhoneOrEmail)) {
            password = userService.searchUserPasswordWithEmail(usernameOrPhoneOrEmail);
            roleName = userService.searchRolenameWithEmail(usernameOrPhoneOrEmail);
        } else {
//            if it is username
            password = userService.searchUserPasswordWithUsername(usernameOrPhoneOrEmail);
            roleName = userService.searchRolenameWithUsername(usernameOrPhoneOrEmail);
        }
        if (password == null) {
            throw new UsernameNotFoundException("does't exit user");
        }
                List < GrantedAuthority > list = new ArrayList<>();
        roleName.forEach(p -> list.add((GrantedAuthority) () -> p));
        return new UserDetailsCustomer(usernameOrPhoneOrEmail, password, ImmutableList.copyOf(list));
    }


}
