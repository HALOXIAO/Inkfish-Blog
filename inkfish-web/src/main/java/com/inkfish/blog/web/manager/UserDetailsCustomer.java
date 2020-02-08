package com.inkfish.blog.web.manager;

import com.google.common.collect.ImmutableList;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author HALOXIAO
 **/
public class UserDetailsCustomer implements UserDetails {

    private final String username;
    private final String password;
    private ImmutableList<GrantedAuthority> list;

    public UserDetailsCustomer(String username, String password, ImmutableList<GrantedAuthority> list) {
        this.username = username;
        this.password = password;
        this.list = list;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return list;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
