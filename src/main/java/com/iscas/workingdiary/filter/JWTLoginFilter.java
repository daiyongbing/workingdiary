package com.iscas.workingdiary.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscas.workingdiary.bean.CustomUserDetails;
import com.iscas.workingdiary.bean.ResponseBody;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 */

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
    private Logger log = LoggerFactory.getLogger(getClass());

    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // 接收并解析用户凭证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            CustomUserDetails userDetails = new ObjectMapper()
                    .readValue(request.getInputStream(), CustomUserDetails.class);
            log.info("用户输入的用户名:"+userDetails.getUsername());
            log.info("用户输入的密码:"+userDetails.getPassword());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            userDetails.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 用户成功登录后生成token返回给前端
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        //SecurityContextHolder.getContext().setAuthentication(auth);
        String token = JWTTokenUtil.generateToken(((User)auth.getPrincipal()).getUsername(), null);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setJwtToken("Bearer " + token);
        responseBody.setMessage("登录成功");
        responseBody.setCode(200);
        response.getWriter().write(JSON.toJSONString(responseBody));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(201);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setMessage("用户名或密码错误");
        responseBody.setCode(201);
        response.getWriter().write(JSON.toJSONString(responseBody));
    }
}

