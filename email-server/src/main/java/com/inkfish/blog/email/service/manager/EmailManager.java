package com.inkfish.blog.email.service.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
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
public class EmailManager {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    MailProperties mailProperties;

    @Autowired
    TemplateEngine templateEngine;


    //TODO 回调，以及重试
    public void sendRegisterMail(String targetEmail, String code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(targetEmail);
        helper.setSubject("账号注册");
        Context context = new Context();
        context.setVariable("code", code);
        String mail = templateEngine.process("RegisterMail", context);
        helper.setText(mail, true);
        javaMailSender.send(message);
        log.info(targetEmail + "邮件发送成功");

    }


    public void sendForgetPasswordMail(String targetEmail, String code) throws MessagingException {
        log.debug( mailProperties.toString());
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(targetEmail);
        helper.setSubject("找回密码");
        Context context = new Context();
        context.setVariable("code", code);
        String mail = templateEngine.process("ForgetPasswordMail", context);
        helper.setText(mail, true);
        javaMailSender.send(message);
        log.info(targetEmail + "邮件发送成功");
    }


}
