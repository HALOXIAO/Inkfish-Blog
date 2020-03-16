package com.inkfish.blog.server.web.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import com.inkfish.blog.server.common.SESSION_CODE;
import com.inkfish.blog.server.common.util.CodeVerification;
import com.inkfish.blog.server.mapper.convert.RegisterToUser;
import com.inkfish.blog.server.model.front.Email;
import com.inkfish.blog.server.model.front.RecoverPassword;
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
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

    private final String VERIFICATION_CODE = "verificationCode";


    @ApiOperation(value = "注册账号，根据邮箱注册")
    @PostMapping("/register")
    public ResultBean<String> register(@Valid @RequestBody Register register, BindingResult result) {
        if (result.hasErrors()) {
            ResultBean<String> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
            log.warn(result.getFieldError() == null ? "No Error Information" : result.getFieldErrors().toString());
            return bean;
        }
        if (register.getCode() != httpSession.getAttribute(SESSION_CODE.REGISTER_VERIFICATION_CODE.getValue())) {
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


    /**
     * 发送注册用的邮件
     */
    @PostMapping("/register/code")
    public ResultBean<String> registerCode(@Valid @RequestBody Email email, BindingResult result) {
        if (result.hasErrors()) {
            ResultBean<String> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
            log.warn(result.getFieldError() == null ? "No Error Information" : result.getFieldErrors().toString());
            return bean;
        }
        if (!email.getCode().equals(httpSession.getAttribute(VERIFICATION_CODE))) {
            httpSession.removeAttribute(VERIFICATION_CODE);
            return new ResultBean<>("code error", RESULT_BEAN_STATUS_CODE.CHECK_FAIL);
        }
        try {
            String code = emailService.registerCode(email.toString());
            httpSession.setAttribute(SESSION_CODE.REGISTER_VERIFICATION_CODE.getValue(), code);
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
        httpSession.setAttribute(VERIFICATION_CODE, text);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            ImageIO.write(image, "JPG", outputStream);
            outputStream.flush();
        }

    }

    @ApiOperation("用于验证密码，在忘记密码的情况下使用，将会验证所输入的验证码是否匹配")
    @PutMapping("/recover/password")
    public ResultBean<Boolean> forgetPassword(@RequestBody @Valid RecoverPassword recoverPassword, BindingResult result) {
        if (result.hasErrors()) {
            ResultBean<Boolean> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
            log.warn(result.getFieldError() == null ? "No Error Information" : result.getFieldErrors().toString());
            return bean;
        }
        if (!httpSession.getAttribute(SESSION_CODE.RECOVER_VERIFICATION_CODE.getValue()).equals(recoverPassword.getCode())) {
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
        }
        String email = (String) httpSession.getAttribute("email");
        if (!userService.updatePasswordWithEmail(email, recoverPassword.getPassword())) {
            return new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
        }
        return new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
    }


    @ApiOperation(value = "用于找回密码，在忘记密码的情况下使用，将会发送验证码至指定邮箱")
    @GetMapping("/recover/password")
    public ResultBean<String> forgetPasswordCode(@Valid @RequestBody Email email, BindingResult result) {
        if (result.hasErrors()) {
            ResultBean<String> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
            log.warn(result.getFieldError() == null ? "No Error Information" : result.getFieldErrors().toString());
            return bean;
        }
        if (!email.getCode().equals(httpSession.getAttribute(VERIFICATION_CODE))) {
            httpSession.removeAttribute(VERIFICATION_CODE);
            return new ResultBean<>("code fail", RESULT_BEAN_STATUS_CODE.CHECK_FAIL);
        }
        try {
            String code = emailService.recoverPassword(email.getEmail());
            httpSession.setAttribute(SESSION_CODE.RECOVER_VERIFICATION_CODE.getValue(), code);
            httpSession.setAttribute("email", email);
            ResultBean<String> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
            bean.setData(code);
            return bean;
        } catch (InterruptedException | RemotingException | MQClientException | MQBrokerException e) {
            log.error(e.getMessage(), e.getCause());
            return new ResultBean<>("Remoting system error", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
        }
    }

    @ApiOperation(value = "Github登陆")
    @PostMapping("")
    public ResultBean<String> test() {
        return null;
    }


}
