package com.iscas.workingdiary.util.jjwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * JWT工具类，使用用户证书替代字符串密钥，安全性更高
 */
public class JWTTokenUtil {
    private static InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("mykeystore_1.jks");
    private static PrivateKey privateKey = null;
    private static PublicKey publicKey = null;
    private static Long expirationSeconds = 1000*60*30L;

    static {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, "123".toCharArray());
            privateKey = (PrivateKey) keyStore.getKey("1", "123".toCharArray());
            publicKey = keyStore.getCertificate("1").getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateToken(String subject, Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds))
                //.signWith(SignatureAlgorithm.RS256, privateKey)
                .signWith(SignatureAlgorithm.ES256, privateKey) //ECDSASHA256加密算法，同时也是RepChain支持的证书加密算法
                .compact();
    }

    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token.replace("Bearer ", "")).getBody();
        } catch (MalformedJwtException | ExpiredJwtException | SignatureException e){    //token格式异常
            return null;
        }
    }
}
