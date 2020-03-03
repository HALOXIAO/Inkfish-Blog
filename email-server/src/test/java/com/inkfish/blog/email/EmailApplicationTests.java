package com.inkfish.blog.email;

import com.inkfish.blog.email.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author HALOXIAO
 **/
@SpringBootTest
public class EmailApplicationTests {

    @Autowired
    EmailService emailService;

    @Test
    public void contextLoads() {
        emailService.emailService();
    }
}
