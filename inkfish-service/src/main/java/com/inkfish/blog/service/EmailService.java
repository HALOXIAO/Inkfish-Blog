package com.inkfish.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;

/**
 * @author HALOXIAO
 **/
@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

}
