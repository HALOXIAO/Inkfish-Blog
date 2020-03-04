package com.inkfish.blog.server.web.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.common.util.CodeVerification;
import com.inkfish.blog.server.mapper.convert.RegisterToUser;
import com.inkfish.blog.server.model.front.Email;
import com.inkfish.blog.server.model.front.Register;
import com.inkfish.blog.server.model.pojo.User;
import com.inkfish.blog.server.service.EmailService;
import com.inkfish.blog.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HALOXIAO
 **/
@Api("用户账号相关操作")
@RestController
@Slf4j
public class UserController {

    @Autowired
    HttpSession httpSession;

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @Autowired
    CodeVerification codeVerification;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;


    @PostMapping("/register")
    public ResultBean<String> register(@Valid @RequestBody Register register, BindingResult result) {
        if (result.hasErrors()) {
            ResultBean<String> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
            log.warn(result.getFieldErrors() == null ? "No Error Information" : result.getFieldErrors().toString());
            return bean;
        }
        if (register.getCode() != httpSession.getAttribute("registerCode")) {
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
        }
        User user = RegisterToUser.INSTANCE.from(register);
        log.info(register.toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userService.addUser(user)) {
            return new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        }
        return new ResultBean<>("register fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
    }


    @PostMapping("/register/code")
    public ResultBean<String> registerCode(@Valid @RequestBody Email email, BindingResult result, HttpServletResponse response, HttpServletRequest request) {
        if (result.hasErrors()) {
            ResultBean<String> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
            log.warn(result.getFieldErrors() == null ? "No Error Information" : result.getFieldErrors().toString());
            return bean;
        }
        try {
            String code = emailService.registerCode(email.toString());
            httpSession.setAttribute("registerCode", code);
            //TODO            此处可以集成监控和报警
        } catch (InterruptedException | RemotingException | MQClientException | MQBrokerException e) {
            log.error(e.getMessage());
            return new ResultBean<>("Remoting system error", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
        }
        return new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
    }


    @GetMapping("/verification")
    @ApiOperation(value = "获取验证码，验证码的文本将放在Header中的'code'里，前端直接匹配即可")
    @ApiResponse(code = 200, message = "返回的图像为jpg格式，有可能返回的Code：成功、未知异常")
    public void verificationCode(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpg");
        String text = defaultKaptcha.createText();
        response.setHeader("code", text);
        BufferedImage image = defaultKaptcha.createImage(text);
        httpSession.setAttribute("verificationCode", text);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            ImageIO.write(image, "JPG", outputStream);
            outputStream.flush();
        }

    }

    @PostMapping("/getpass")
    public ResultBean<String> forgetPassword(@Valid @RequestBody Email email, BindingResult result) {
        if (result.hasErrors()) {
            ResultBean<String> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
            log.warn(result.getFieldErrors() == null ? "No Error Information" : result.getFieldErrors().toString());
            return bean;
        }
        return null;
    }

}
