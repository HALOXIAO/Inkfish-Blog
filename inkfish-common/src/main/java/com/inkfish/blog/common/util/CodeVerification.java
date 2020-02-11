package com.inkfish.blog.common.util;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @author HALOXIAO
 **/
@Component
public class CodeVerification {

    @Autowired
    HttpSession httpSession;

    public boolean isCodeCorrect(String code) {
        return ((String) httpSession.getAttribute("Verification")).equals(code);
    }

}