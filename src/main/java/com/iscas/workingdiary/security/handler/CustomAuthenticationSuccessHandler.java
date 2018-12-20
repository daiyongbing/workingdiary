package com.iscas.workingdiary.security.handler;

import com.alibaba.fastjson.JSON;
import com.iscas.workingdiary.bean.ResponseBody;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResponseBody responseBody = new ResponseBody();

        responseBody.setCode(200);
        responseBody.setMessage("登录成功");
        /*UserDetail userDetails = (UserDetail) authentication.getPrincipal().;
        String jwtToken = JWTTokenUtil.generateToken(userDetails);
        responseBody.setJwtToken(jwtToken);*/
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(responseBody));
    }
}
