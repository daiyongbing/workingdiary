package com.iscas.workingdiary.util;

/**
 * Byte与Hex之间的转换工具类
 */
public class Byte2Hex {
    /**
     * byte[]转Hex
     * @param bytes
     * @return hex
     */
    public static String bytes2Hex( byte[] bytes){
        String hex;
        final StringBuilder hexString = new StringBuilder("");
        if (bytes == null || bytes.length <= 0)
            return null;
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        hex = hexString.toString().toLowerCase();
        return hex;
    }

    /**
     * 代码来源于RCJava
     * @param bytes
     * @return
     */
    public static String bytes2hex(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return buf.toString();
    }

    public static Boolean isEmpty(String str){
        Boolean flag = false;
        if (str == null || str == ""){
            flag = true;
        }
        return flag;
    }

    /**
     * Hex转byte[]
     * @param hexString
     * @return bytes
     */
    public static byte[] hex2bytes(String hexString) {
        if (isEmpty(hexString))
            return null;
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index  > hexString.length() - 1)
                return byteArray;
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }

}
