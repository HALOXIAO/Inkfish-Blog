package com.inkfish.blog.server.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

//TODO  OAUTH2.0 Github


@EnableRedisHttpSession
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.inkfish.blog.server"})
@SpringBootApplication
@MapperScan("com.inkfish.blog.server")
@EnableAspectJAutoProxy
public class InkfishWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(InkfishWebApplication.class, args);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(){
//        return new LettuceConnectionFactory();
//    }
//
//    @Bean
//    public RedisHttpSessionConfiguration redisHttpSessionConfiguration(){
//        return new RedisHttpSessionConfiguration();
//    }

}
