package com.inkfish.blog.web.controller;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.inkfish.blog.model.front.Register;
import com.inkfish.blog.status.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.status.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

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

    @PostMapping("/register")
    public ResultBean<String> register(@Valid @RequestBody Register register, BindingResult result, String code) {
//        if (!code.equals( (String) httpSession.getAttribute("verificationCode"))) {
//
//        }
        if (result.hasErrors()) {
            ResultBean<String> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.ARGUMENT_EXCEPTION);
            log.warn(result.getFieldErrors() == null ? "No Error Information" : result.getFieldErrors().toString());
            return bean;
        }
        return null;
    }

    @GetMapping("/verification")
    public void verificationCode(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String text = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(text);
        ResultBean<BufferedImage> bean = new ResultBean<>(image, RESULT_BEAN_STATUS_CODE.SUCCESS);
        httpSession.setAttribute("verificationCode", text);
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write(JSON.toJSONString(bean));
            printWriter.flush();
        }
    }


}
