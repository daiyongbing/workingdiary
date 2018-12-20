package com.iscas.workingdiary.common;

import com.iscas.workingdiary.config.ConstantProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http请求头公共方法类
 */
public class HttpHeaderFunction {

    @Autowired
    private static ConstantProperties properties;

    public static String getToken(HttpServletRequest request, HttpServletResponse response){

        return null;
    }
}
