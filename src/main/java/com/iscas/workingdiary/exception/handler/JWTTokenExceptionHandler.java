package com.iscas.workingdiary.exception.handler;

import com.alibaba.fastjson.JSON;
import com.iscas.workingdiary.bean.ResponseBody;
import com.iscas.workingdiary.bean.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理JWTToken异常
 */
public class JWTTokenExceptionHandler {

    /**
     * 没有token
     * @param response
     */
    public static void noAuthorizationException(HttpServletResponse response) throws IOException{
        response.setStatus(ResponseStatus.CLIENT_JWTTOKEN_NULL_ERROR);
        response.setContentType("application/json;charset=UTF-8");
        ResponseBody responseBody = new ResponseBody();
        responseBody.setMessage("No Authorization");
        responseBody.setCode(401);
        response.getWriter().write(JSON.toJSONString(responseBody));
    }


    /**
     * 无效的token
     * @param response
     */
    public static void invalidAuthorizationException(HttpServletResponse response){
        response.setStatus(ResponseStatus.CLIENT_JWTTOKEN_NULL_ERROR);
        response.setContentType("application/json;charset=UTF-8");
        ResponseBody responseBody = new ResponseBody();
        responseBody.setCode(401);
        responseBody.setMessage("Invalid Authorization");
        try {
            response.getWriter().write(JSON.toJSONString(responseBody));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * token不是Bearer类型的
     * @param response
     */
    public static void invalidAuthFormatException(HttpServletResponse response){
        response.setStatus(ResponseStatus.CLIENT_JWTTOKEN_NULL_ERROR);
        response.setContentType("application/json;charset=UTF-8");
        ResponseBody responseBody = new ResponseBody();
        responseBody.setMessage("not Bearer token");
        responseBody.setCode(401);
        try {
            response.getWriter().write(JSON.toJSONString(responseBody));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * token过期
     * @param response
     */
    public static void expiredAuthorizationException(HttpServletResponse response){
        response.setStatus(ResponseStatus.CLIENT_JWTTOKEN_NULL_ERROR);
        response.setContentType("application/json;charset=UTF-8");
        ResponseBody responseBody = new ResponseBody();
        responseBody.setMessage("Expired Authorization");
        responseBody.setCode(401);
        try {
            response.getWriter().write(JSON.toJSONString(responseBody));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
