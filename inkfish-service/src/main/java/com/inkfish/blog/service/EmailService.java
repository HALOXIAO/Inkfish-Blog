package com.inkfish.blog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private JavaMailSender javaMailSender;


    //TODO 回调，以及重试
    private void sendHtmlMail(String to, String subject, String content) {
        try {
            //true表示需要创建一个multipart message
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom("584714929@qq.com");
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(message);
            log.info("html邮件发送成功");
        } catch (MessagingException e) {
            log.error("发送html邮件时发生异常！", e);
        }
    }

    public void sendRegisterMail(String targetEmail,String code){
        Context context = new Context();
        TemplateEngine templateEngine = new TemplateEngine();
        String emailContext = templateEngine.process("RegisterMail.html",context);
        log.info(emailContext.toString());
        sendHtmlMail(targetEmail,"验证码",emailContext);
    }


}
