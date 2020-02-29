package com.inkfish.blog.web.config;

import com.inkfish.blog.web.handler.exceptionHandler.*;
import com.inkfish.blog.web.manager.UserDetailsServiceCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author HALOXIAO
 **/
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    LoginFallHandler loginFallHandler;

    @Autowired
    LogoutSuccessHandlerImp logoutSuccessHandlerImp;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    NoLoginHandler noLoginHandler;

    @Autowired
    AccessDeniedHandlerImp accessDeniedHandlerImp;

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
        http.logout().logoutUrl("/logout").permitAll().logoutSuccessHandler(logoutSuccessHandlerImp)
                .and()
                .formLogin().loginPage("/login").permitAll().successHandler(loginSuccessHandler).failureHandler(loginFallHandler)
                .and()
                .csrf().disable().authorizeRequests().antMatchers("/login", "/logout", "/register", "/verification", "/home").permitAll();
        http.exceptionHandling().authenticationEntryPoint(noLoginHandler).accessDeniedHandler(accessDeniedHandlerImp);
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


}
