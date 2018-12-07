package com.iscas.workingdiary.security.handler;

import com.alibaba.fastjson.JSON;
import com.iscas.workingdiary.bean.ResponseBody;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.jwtsecurity.JWTHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        ResponseBody responseBody = new ResponseBody();

        responseBody.setStatus("200");
        responseBody.setMessage("登录成功");
        User userDetails = (User) authentication.getPrincipal();
        String jwtToken = JWTHelper.getToken(userDetails);
        responseBody.setJwtToken(jwtToken);
        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
    }
}
