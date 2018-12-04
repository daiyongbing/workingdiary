package com.iscas.workingdiary.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class DESScrypt {
    public static String encrypt2toString(byte[] source, String key) {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            bytes = cipher.doFinal(source);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(bytes);
    }


    public static byte[] decrypt2bytes(String key, String source) {
        byte[] bytes = null;
        try {
            SecretKey deskey = new SecretKeySpec(key.getBytes(), "DESede");
            Cipher c1 = Cipher.getInstance("DESede");
            byte[] decode = Base64.getDecoder().decode(source);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            bytes = c1.doFinal(decode);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return bytes;
    }

    public static void main(String[] args){
        String text = "hello";
        String key = "cb707fb730e3c0e01b3657c6633f510e";
        String encrypted = DESScrypt.encrypt2toString(text.getBytes(), key);
        System.out.println("encrypted："+encrypted);
        byte[] decrypted = decrypt2bytes(key, encrypted);
        System.out.println(decrypted.toString());
    }
}
