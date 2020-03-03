package com.inkfish.blog.server.web.handler.exceptionHandler;

import com.alibaba.fastjson.JSON;
import com.inkfish.blog.server.common.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.server.common.ResultBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
public class AccessDeniedHandlerImp implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        try (PrintWriter printWriter = response.getWriter()){
            ResultBean<String> bean = new ResultBean<>("fail", RESULT_BEAN_STATUS_CODE.NO_PERMISSION);
            printWriter.write(JSON.toJSONString(bean));
            printWriter.flush();
        }
    }
}
