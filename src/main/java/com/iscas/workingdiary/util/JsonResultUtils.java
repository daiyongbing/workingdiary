package com.iscas.workingdiary.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonResultUtils {
    public static void returnJson(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json; charset=utf-8");
        /*try {
            response.getWriter().write(resultJson(response, request,resultDto));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
