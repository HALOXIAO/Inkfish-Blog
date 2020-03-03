package com.inkfish.blog.email.config;

import com.inkfish.blog.email.service.EmailService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author HALOXIAO
 **/
public class RocketMQStartListener implements ApplicationListener<ApplicationReadyEvent> {


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        EmailService emailService = applicationContext.getBean(EmailService.class);
        emailService.forgetPasswordEmailService();
    }
}
