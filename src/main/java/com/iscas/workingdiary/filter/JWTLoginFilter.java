package com.iscas.workingdiary.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscas.workingdiary.bean.ResponseBody;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
            User user = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);
            log.info("用户输入的用户名:"+user.getUserName());
            log.info("用户输入的密码:"+user.getPassword());
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUserName(),
                            user.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 用户成功登录后生成token返回给前端
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        String token = JWTTokenUtil.generateToken(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername(), null);
        response.addHeader("Authorization", "Bearer " + token);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("200");
        responseBody.setMessage("登录成功");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(responseBody));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ResponseBody responseBody = new ResponseBody();
        responseBody.setMessage("用户名或密码错误");
        responseBody.setStatus("201");
        response.getWriter().write(JSON.toJSONString(responseBody));
    }
}

