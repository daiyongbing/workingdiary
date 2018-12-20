package com.iscas.workingdiary.security.handler;

import com.alibaba.fastjson.JSON;
import com.iscas.workingdiary.bean.ResponseBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setCode(100);
        responseBody.setMessage("退出登录");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(responseBody));
    }
}
