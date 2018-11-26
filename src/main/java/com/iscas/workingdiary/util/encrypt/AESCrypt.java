package com.iscas.workingdiary.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
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
    public static String AESEncrypt(String sourceStr) throws Exception {
        byte[] keyBytes = CRYPTKEY.getBytes("utf-8");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] aesEncrypted = cipher.doFinal(sourceStr.getBytes("utf-8"));
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

    public static void main(String[] args) throws Exception {
        String cSrc = "HELLO";
        String enString = AESEncrypt(cSrc);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = AESDecrypt(enString);
        System.out.println("解密后的字串是：" + DeString);
    }
}
