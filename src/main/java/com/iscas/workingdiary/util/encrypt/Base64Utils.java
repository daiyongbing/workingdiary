package com.iscas.workingdiary.util.encrypt;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64工具类
 */
public class Base64Utils {

    public static String encode2String(String source){
        return Base64.getEncoder().encodeToString(source.getBytes());
    }

    public static String encode2String(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] encode2Bytes(String source){
        return Base64.getEncoder().encode(source.getBytes());
    }
    public static byte[] encode2Bytes(byte[] bytes){
        return Base64.getEncoder().encode(bytes);
    }


    public static byte[] decode2Bytes(String source){
        return Base64.getDecoder().decode(source);
    }

    public static byte[] decode2Bytes(byte[] bytes){
        return Base64.getDecoder().decode(bytes);
    }

    public static String decode2String(String str) {
        String decodedStr = "";
        try {
            decodedStr =  new String(Base64.getDecoder().decode(str), "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedStr;
    }

    public static String decode2String(byte[] bytes) {
        String decodedStr = "";
        try {
            decodedStr =  new String(Base64.getDecoder().decode(bytes), "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedStr;
    }
}
