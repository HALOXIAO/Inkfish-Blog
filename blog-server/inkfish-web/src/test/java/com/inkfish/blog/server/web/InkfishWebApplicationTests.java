package com.inkfish.blog.server.web;

import com.inkfish.blog.server.service.manager.EmailManager;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;


@SpringBootTest
@Component
class InkfishWebApplicationTests {

    @Autowired
    EmailManager emailManager;


    @Test
    void contextLoads() throws  InterruptedException, RemotingException, MQClientException, MQBrokerException {
        emailManager.sendForgetPasswordEmail("haloxql@gmail.com","aasdwxsd");
    }
}

