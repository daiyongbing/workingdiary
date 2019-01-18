package com.iscas.workingdiary.util.encrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * AES加解密实现类
 */
public final class AESCrypt {
    private static final String CRYPTKEY = "kj34PXF65ze70uFG";

    /**
     * AES加密
     * @param sourceStr 加密源
     * @return base64Encoded 编码后的AES加密字符串
     * @throws Exception
     */
    public static String AESEncrypt(String sourceStr) {
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = CRYPTKEY.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = null;
        byte[] aesEncrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            aesEncrypted = cipher.doFinal(sourceStr.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String base64Encoded = Base64.getEncoder().encodeToString(aesEncrypted);
        return base64Encoded;
    }

    /**
     * AES解密算法
     * @param base64Str
     * @return
     * @throws Exception
     */
    public static String AESDecrypt(String base64Str) throws Exception {
        try {
            byte[] keyBytes = CRYPTKEY.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted = Base64.getDecoder().decode(base64Str);
            try {
                byte[] original = cipher.doFinal(encrypted);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    /**
     * 自定义密钥加密成字符串
     * @param src
     * @param password
     * @return
     * @throws Exception
     */
    public static String encrypt2String(String src, String password) throws Exception {
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = password.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = null;
        byte[] aesEncrypted = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            aesEncrypted = cipher.doFinal(src.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String base64Encoded = Base64.getEncoder().encodeToString(aesEncrypted);
        return base64Encoded;
    }

    /**
     * 解密
     * @param encryptedStr
     * @param password
     * @return
     * @throws Exception
     */
    public static String decrypt2String(String encryptedStr, String password) throws Exception{
        try {
            byte[] keyBytes = password.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted = Base64.getDecoder().decode(encryptedStr);
            byte[] original = cipher.doFinal(encrypted);
            String originalString = new String(original,"utf-8");
            return originalString;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    /**
     * 自定义加密密钥加密
     * @param bytes
     * @param encypt_password
     * @return
     */
    public static byte[] encrypt2Bytes(byte[] bytes, String encypt_password){
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = encypt_password.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = null;
        byte[] aesEncrypted = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            aesEncrypted = cipher.doFinal(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aesEncrypted;
    }

    /**
     * 自定义密钥解密
     * @param bytes
     * @param decrypt_password
     * @return
     */
    public static byte[] decrypt2bytes(byte[] bytes, String decrypt_password){
        try {
            byte[] keyBytes = decrypt_password.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            try {
                byte[] original = cipher.doFinal(bytes);
                return original;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String cSrc = "123";
        String password = "123456";
        String enString = AESEncrypt(cSrc);
        System.out.println("加密后的字符串：" + enString);

        // 解密
        String DeString = AESDecrypt(enString);
        System.out.println("解密后的字符串：" + DeString);

        //自定义密钥
        String encrypt = AESCrypt.encrypt2String(cSrc, MD5Utils.crypt16Byte(password));
        System.out.println("自定义密钥加密后："+encrypt);

        String decrypt = AESCrypt.decrypt2String(encrypt, MD5Utils.crypt16Byte(password));
        System.out.println("自定义密钥解密后："+decrypt);
    }
}
