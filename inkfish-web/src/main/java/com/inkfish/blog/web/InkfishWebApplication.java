package com.inkfish.blog.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.inkfish.blog")
@ComponentScan(basePackages = {"com.inkfish.blog"})
public class InkfishWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(InkfishWebApplication.class, args);
    }

}
