package com.iscas.workingdiary.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jmx.snmp.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

public class JsonResult {
    public static String resultJson(HttpServletResponse response, HttpServletRequest request, ResultData resultData){
        String back = request.getParameter("callback");
        Gson gson =  new GsonBuilder().registerTypeAdapter(Timestamp.class, new SimpleDateFormat("yyyy-MM-dd")).setDateFormat("yyyy-MM-dd").create();
        String result = "";
        if(back == null || back.equals("")){
            result =  gson.toJson(resultData);
        }else{
            result =  back+"("+gson.toJson(resultData)+")";//跨域访问请求返回数据
        }
        return result;
    }


    /*
     * 编码
     */
    public String encode2UTF8(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void resultString(HttpServletResponse response,HttpServletRequest request, String str){
        response.setContentType("application/json; charset=utf-8");
        String back = request.getParameter("callback");
        String result = "";
        if(back == null || back.equals("")){
            result =  str;
        }else{
            result =  back+"("+str+")";//跨域访问请求返回数据
        }
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public  static void result(HttpServletResponse response,HttpServletRequest request, ResultData resultData){

        response.setContentType("application/json; charset=utf-8");
        try {
            response.getWriter().write(resultJson(response, request,resultData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
