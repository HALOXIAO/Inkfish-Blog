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
    public EmailService(EmailManager emailManager, StringRedisTemplate stringRedisTemplate,MqConfig mqConfig) {
        this.emailManager = emailManager;
        this.stringRedisTemplate = stringRedisTemplate;
        this.mqConfig = mqConfig;
    }

    private MessageListenerConcurrently messageListenerConcurrently() {
        return new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    String nameSpace = "forgetPassword".equals(msg.getTopic()) ? REDIS_NAMESPACE.EMAIL_VERIFICATION_FORGET_PASSWORD_NAMESPACE.getValue() : REDIS_NAMESPACE.EMAIL_VERIFICATION_REGISTER_NAMESPACE.getValue();
                    String body = new String(msg.getBody(), StandardCharsets.UTF_8);
                    if (!stringRedisTemplate.hasKey(nameSpace + body)) {
                        continue;
                    }
                    String code = stringRedisTemplate.opsForValue().get(nameSpace + body);
                    stringRedisTemplate.delete(nameSpace + body);
                    try {
                        if (nameSpace.equals(REDIS_NAMESPACE.EMAIL_VERIFICATION_FORGET_PASSWORD_NAMESPACE.getValue())) {
                            emailManager.sendForgetPasswordMail(body, code);
                        } else {
                            emailManager.sendRegisterMail(body, code);
                        }
                    } catch (MessagingException e) {
                        log.error("发送邮件错误: " + e.getMessage());
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        };
    }


    public void emailService() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(mqConfig.getNameSrvAddress());
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setConsumerGroup("EMAIL");
        try {
            consumer.subscribe("email", "forgetPassword||register");
            consumer.registerMessageListener(messageListenerConcurrently());
            consumer.start();
        } catch (MQClientException e) {
            log.error("Client异常：" + e.getErrorMessage() + " Error Code:" + e.getResponseCode());
            consumer.shutdown();
        }
    }


}









