package com.inkfish.blog.server.web.handler.exceptionHandler;

import com.alibaba.fastjson.JSON;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author HALOXIAO
 **/
@Component
public class NoLoginHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write(JSON.toJSONString(new ResultBean<String>("no login", RESULT_BEAN_STATUS_CODE.NO_LOGIN)));
            printWriter.flush();
        }
}
}
