package com.inkfish.blog.web.config;

import com.inkfish.blog.web.handle.LoginFallHandler;
import com.inkfish.blog.web.handle.LoginSuccessHandler;
import com.inkfish.blog.web.handle.LogoutSuccessHandlerImp;
import com.inkfish.blog.web.handle.NoLoginHandler;
import com.inkfish.blog.web.manager.UserDetailsServiceCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author HALOXIAO
 **/
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    LoginFallHandler loginFallHandler;

    @Autowired
    LogoutSuccessHandlerImp logoutSuccessHandlerImp;

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceCustomer();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new NoLoginHandler();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.logout().logoutSuccessUrl("/logout").logoutSuccessHandler(logoutSuccessHandlerImp)
                .and()
                .formLogin().loginProcessingUrl("/login").successHandler(loginSuccessHandler).failureHandler(loginFallHandler)
                .and()
                .authorizeRequests().antMatchers("/login","/logout").permitAll();
    }
}
