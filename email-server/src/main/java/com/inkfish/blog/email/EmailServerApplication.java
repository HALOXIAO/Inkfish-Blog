package com.inkfish.blog.email;


import com.inkfish.blog.email.config.RocketMQStartListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HALOXIAO
 **/
@SpringBootApplication
public class EmailServerApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(EmailServerApplication.class);
        application.addListeners(new RocketMQStartListener());
        application.run(args);
    }
}
