package com.inkfish.email.service;

import com.inkfish.email.service.manager.EmailManager;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HALOXIAO
 **/
@Service
public class MqService {

    private EmailManager emailManager;

    @Autowired
    public MqService(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public void Email() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr("localhost:9876");


    }

}
