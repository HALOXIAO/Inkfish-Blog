package com.inkfish.email.service;

import com.inkfish.blog.common.REDIS_NAMESPACE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author HALOXIAO
 **/
@Service
@Slf4j
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    MailProperties mailProperties;

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //TODO 回调，以及重试
    protected void sendRegisterMail(String targetEmail) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(targetEmail);
            helper.setFrom(mailProperties.getUsername());
            helper.setSubject("账号注册");
            Context context = new Context();
            String code = stringRedisTemplate.opsForValue().get(REDIS_NAMESPACE.EMAIL_VERIFICATION_REGISTER_NAMESPACE.getValue() + targetEmail);
            context.setVariable("code", code);
            String mail = templateEngine.process("RegisterMail",context);
            helper.setText(mail, true);
            javaMailSender.send(message);
            log.info(targetEmail + "邮件发送成功");
        } catch (MessagingException e) {
            log.error("发送Register邮件时发生异常！", e);
        }
    }


    protected void sendForgetPasswordMail(String targetEmail){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(targetEmail);
            helper.setFrom(mailProperties.getUsername());
            helper.setSubject("找回密码");
            Context context = new Context();
            String code = stringRedisTemplate.opsForValue().get(REDIS_NAMESPACE.EMAIL_VERIFICATION_FORGET_PASSWORD_NAMESPACE.getValue() + targetEmail);
            context.setVariable("code", code);
            String mail = templateEngine.process("ForgetPasswordMail",context);
            helper.setText(mail, true);
            javaMailSender.send(message);
            log.info(targetEmail + "邮件发送成功");
        } catch (MessagingException e) {
            log.error("发送ForgetPassword邮件时发生异常！", e);
        }
    }


}
