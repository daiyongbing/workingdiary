package com.iscas.workingdiary.util.json;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jmx.snmp.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

public class JsonResult {
    public static void resultJson(HttpServletResponse response, HttpServletRequest request, int status, Object result){
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(status);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
