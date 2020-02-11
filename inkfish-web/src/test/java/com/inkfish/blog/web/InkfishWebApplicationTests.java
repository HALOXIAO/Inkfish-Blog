package com.inkfish.blog.web;

import com.inkfish.blog.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@Component
class InkfishWebApplicationTests {

    @Autowired
    EmailService emailService;

    @Test
    void contextLoads() {
        Context context = new Context();
        TemplateEngine templateEngine = new TemplateEngine();
        String emailContext = templateEngine.process("Register",context);
        System.out.println(emailContext);
    }

}