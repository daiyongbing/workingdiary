package com.iscas.workingdiary.filter;

import com.alibaba.fastjson.JSON;
import com.iscas.workingdiary.bean.ResponseBody;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 实现token的校验功能
 */

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("application/json;char-set=utf8");
        String authHeader = request.getHeader("Authorization");
        ResponseBody responseBody = new ResponseBody();
        if (authHeader == null) { // 未提供Token
            responseBody.setStatus("403");
            responseBody.setMessage("no token");
            response.getWriter().write(JSON.toJSONString(responseBody));
            return;
        }
        if (!authHeader.startsWith("Bearer ")) {
            responseBody.setStatus("403");
            responseBody.setMessage("error token format");
            response.getWriter().write(JSON.toJSONString(responseBody));
            return;
        }
        Claims claims = JWTTokenUtil.parseToken(authHeader);
        if (claims == null){
            responseBody.setStatus("403");
            responseBody.setMessage("invalid token");
            response.getWriter().write(JSON.toJSONString(responseBody));
            return;
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }
}
