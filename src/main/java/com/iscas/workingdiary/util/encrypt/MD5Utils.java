package com.iscas.workingdiary.util.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    protected static MessageDigest messagedigest = null;


    static {
        try {
			messagedigest = MessageDigest.getInstance("MD5");
            //messagedigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("MD5FileUtil messagedigest初始化失败");
            e.printStackTrace();
        }
    }


    public static String fileMD5(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        messagedigest.update(byteBuffer);
        return bufferToHex(messagedigest.digest());
    }


    public static String stringMD5(String s) {
        return bytesMD5(s.getBytes());
    }

    /**
     * 取stringMD5的中间16位
     * @param source
     * @return
     */
    public static String crypt16Byte(String source){
        String md5 = stringMD5(source);
        return md5.substring(8, 24);
    }

    public static String bytesMD5(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            char c0 = hexDigits[(bytes[l] & 0xf0) >> 4];
            char c1 = hexDigits[bytes[l] & 0xf];
            stringbuffer.append(c0);
            stringbuffer.append(c1);
        }
        return stringbuffer.toString();
    }




    public static void main(String[] args) throws IOException {
        String str = "12dcidfuiafornfgibfi3";
        System.out.println("MD5：" + stringMD5(str));
        System.out.println("16byte:"+crypt16Byte(str));
    }
}
