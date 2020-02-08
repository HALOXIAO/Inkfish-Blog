package com.inkfish.blog.web.handle;

import com.alibaba.fastjson.JSON;
import com.inkfish.blog.status.RESULT_BEAN_STATUS_CODE;
import com.inkfish.blog.status.ResultBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
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
public class LogoutSuccessHandlerImp implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try (PrintWriter printWriter = response.getWriter()){
            ResultBean<String>bean = new ResultBean<>("success", RESULT_BEAN_STATUS_CODE.SUCCESS);
            printWriter.write(JSON.toJSONString(bean));
            printWriter.flush();
        }
    }
}
