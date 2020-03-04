package com.inkfish.blog.email.service;

import com.inkfish.blog.email.config.MqConfig;
import com.inkfish.blog.server.common.REDIS_NAMESPACE;
import com.inkfish.blog.email.service.manager.EmailManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author HALOXIAO
 **/
@Service
@Slf4j
public class EmailService {

    private EmailManager emailManager;
    private StringRedisTemplate stringRedisTemplate;
    private MqConfig mqConfig;

    @Autowired
    public EmailService(EmailManager emailManager, StringRedisTemplate stringRedisTemplate, MqConfig mqConfig) {
        this.emailManager = emailManager;
        this.stringRedisTemplate = stringRedisTemplate;
        this.mqConfig = mqConfig;
    }

    private MessageListenerConcurrently forgetPasswordMessageListenerConcurrently() {
        return new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    String nameSpace = REDIS_NAMESPACE.EMAIL_VERIFICATION_FORGET_PASSWORD_NAMESPACE.getValue();
                    String body = new String(msg.getBody(), StandardCharsets.UTF_8);
                    if (!stringRedisTemplate.hasKey(nameSpace + body)) {
                        continue;
                    }
                    String code = stringRedisTemplate.opsForValue().get(nameSpace + body);
                    stringRedisTemplate.delete(nameSpace + body);
                    try {
                        emailManager.sendForgetPasswordMail(body, code);
                    } catch (MessagingException e) {
                        log.error("发送邮件错误: " + e.getMessage());
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        };
    }

    private MessageListenerConcurrently registerMessageListenerConcurrently() {
        return new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    String nameSpace = REDIS_NAMESPACE.EMAIL_VERIFICATION_REGISTER_NAMESPACE.getValue();
                    String body = new String(msg.getBody(), StandardCharsets.UTF_8);
                    if (!stringRedisTemplate.hasKey(nameSpace + body)) {
                        continue;
                    }
                    String code = stringRedisTemplate.opsForValue().get(nameSpace + body);
                    stringRedisTemplate.delete(nameSpace + body);
                    try {
                        emailManager.sendRegisterMail(body, code);
                    } catch (MessagingException e) {
                        log.error("发送邮件错误: " + e.getMessage());
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        };
    }


    public void forgetPasswordEmailService() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr("localhost:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setConsumerGroup("email_consumer_forgetPassword");
        try {
            consumer.subscribe("email", "forgetPassword");
            consumer.registerMessageListener(forgetPasswordMessageListenerConcurrently());
            consumer.start();
        } catch (MQClientException e) {
            log.error("Client异常：" + e.getErrorMessage() + " Error Code:" + e.getResponseCode());
            e.printStackTrace();
            consumer.shutdown();
        }
    }


    public void registerEmailService() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr("localhost:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setConsumerGroup("email_consumer_register");
        try {
            consumer.subscribe("email", "register");
            consumer.registerMessageListener(registerMessageListenerConcurrently());
            consumer.start();
        } catch (MQClientException e) {
            log.error("Client异常：" + e.getErrorMessage() + " Error Code:" + e.getResponseCode());
            e.printStackTrace();
            consumer.shutdown();
        }
    }

}









