package com.inkfish.blog.web.handle;


import com.alibaba.fastjson.JSON;
import com.inkfish.blog.service.manager.ResultBean;
import com.inkfish.blog.web.status.RESULT_BEAN_STATUS_CODE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try (PrintWriter printWriter = response.getWriter()) {
            ResultBean<String> bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
            printWriter.write(JSON.toJSONString(bean));
            printWriter.flush();
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }


}