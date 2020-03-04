package com.inkfish.blog.email;

import com.inkfish.blog.email.service.EmailService;
import com.inkfish.blog.email.service.manager.EmailManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

/**
 * @author HALOXIAO
 **/
@SpringBootTest
public class EmailApplicationTests {

    @Autowired
    EmailManager emailManager;

    @Test
    public void contextLoads()throws MessagingException {
        emailManager.sendForgetPasswordMail("haloxql@gmail.com","123");

    }
}
