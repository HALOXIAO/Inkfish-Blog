package com.inkfish.blog.web.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.inkfish.blog.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.common.ResultBean;
import com.inkfish.blog.common.util.CodeVerification;
import com.inkfish.blog.mapper.convert.RegisterToUser;
import com.inkfish.blog.model.front.Register;
import com.inkfish.blog.model.pojo.User;
import com.inkfish.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author HALOXIAO
 **/
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


    //TODO Verification Code
    @PostMapping("/register")
    public ResultBean<String> register(@Valid @RequestBody Register register, BindingResult result) {
        if (result.hasErrors()) {
            ResultBean<String> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
            log.warn(result.getFieldErrors() == null ? "No Error Information" : result.getFieldErrors().toString());
            return bean;
        }
        User user = RegisterToUser.INSTANCE.from(register);
        log.info(register.toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userService.addUser(user)) {
            return new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
        }
        return new ResultBean<>("register fail", RESULT_BEAN_STATUS_CODE.UNKNOWN_EXCEPTION);
    }

    @GetMapping("/verification")
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
    public ResultBean<String> forgetPassword(@Valid @RequestBody @Email String email, BindingResult bindingResult) {

        return null;
    }

}
