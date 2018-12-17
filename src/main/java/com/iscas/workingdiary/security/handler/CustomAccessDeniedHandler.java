package com.iscas.workingdiary.security.handler;

import com.alibaba.fastjson.JSON;
import com.iscas.workingdiary.bean.ResponseBody;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问权限处理类
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus("304");
        responseBody.setMessage("拒绝访问");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(responseBody));
    }
}
