package com.inkfish.blog.service.manager;

import com.inkfish.blog.common.REDIS_NAMESPACE;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author HALOXIAO
 **/
@Service
@Slf4j
public class EmailManager {

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    EmailManager(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //注意，消息幂等性完全由Consumer决定
    public void sendForgetPasswordEmail(String email, String verificationCode) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        stringRedisTemplate.opsForValue().set(REDIS_NAMESPACE.EMAIL_VERIFICATION_FORGET_PASSWORD_NAMESPACE + email, verificationCode);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");//设置日期格式
        String key = email + "-" + LocalDateTime.now().format(fmt);
        DefaultMQProducer producer = new DefaultMQProducer("email");
        producer.setRetryTimesWhenSendFailed(3);
        producer.setNamesrvAddr("localhost:9876");
        producer.setInstanceName("Email");
        Message message = new Message("email", "email-forgetPassword", key, email.getBytes());
        producer.start();
        SendResult sendResult = producer.send(message, 3000L);
        //TODO 可以对sendResult做一些处理
        log.info("Type:ForgetPasswordEmail " + "key: " + key + " SendResult:" + sendResult.getSendStatus());
    }

    public void sendRegisterEmail(String email, String verificationCode) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        stringRedisTemplate.opsForValue().set(REDIS_NAMESPACE.EMAIL_VERIFICATION_REGISTER_NAMESPACE + email, verificationCode);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");//设置日期格式
        String key = email + "-" + LocalDateTime.now().format(fmt);
        DefaultMQProducer producer = new DefaultMQProducer("email");
        producer.setRetryTimesWhenSendFailed(3);
        producer.setNamesrvAddr("localhost:9876");
        producer.setInstanceName("Email");
        Message message = new Message("email", "email-register", key, email.getBytes());
        producer.start();
        SendResult sendResult = producer.send(message, 3000L);
        log.info("Type:RegisterEmail " + "key: " + key + " SendResult:" + sendResult.getSendStatus());
        //TODO 可以对sendResult做一些处理

    }
}
