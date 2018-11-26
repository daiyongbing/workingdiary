package com.iscas.workingdiary.util.encrypt;

import java.util.Base64;

/**
 * Base64工具类
 */
public class Base64Utils {
    /**
     * 字符串编码成字符串
     * @param source
     * @return
     */
    public static String enCode(String source){
        return Base64.getEncoder().encodeToString(source.getBytes());
    }

    /**
     * byte[]编码成byte[]
     * @param bytes
     * @return
     */
    public static byte[] encode(byte[] bytes){
        return Base64.getEncoder().encode(bytes);
    }

    /**
     * 字符串解密成byte[]
     * @param source
     * @return
     */
    public static byte[] decode(String source){
        return Base64.getDecoder().decode(source);
    }

    /**
     * byte[]解密成byte[]
     * @param bytes
     * @return
     */
    public static byte[] decode(byte[] bytes){
        return Base64.getDecoder().decode(bytes);
    }
}
