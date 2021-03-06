package com.inkfish.blog.server.web.handler.exceptionHandler;


import com.alibaba.fastjson.JSON;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author HALOXIAO
 **/
@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try (PrintWriter printWriter = response.getWriter()) {
            ResultBean<String> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
            printWriter.write(JSON.toJSONString(bean));
            printWriter.flush();
        }
    }


}