package com.inkfish.blog.server.service;

import com.inkfish.blog.server.service.manager.EmailManager;
import net.bytebuddy.utility.RandomString;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author HALOXIAO
 **/
@Service
public class EmailService {

    private EmailManager emailManager;

    @Autowired
    public EmailService(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public String registerCode(String email) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        String code = RandomString.make(5);
        emailManager.sendRegisterEmail(email, code);
        return code;
    }

    public String recoverPassword(String email) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        String code = RandomString.make(5);
        emailManager.sendForgetPasswordEmail(email, code);
        return code;
    }

}