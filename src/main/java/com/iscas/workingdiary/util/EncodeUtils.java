package com.iscas.workingdiary.util;

public class EncodeUtils {
    public static String bytes2hex(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return buf.toString();
    }
}
