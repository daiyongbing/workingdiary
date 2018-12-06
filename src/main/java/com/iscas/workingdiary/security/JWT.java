package com.iscas.workingdiary.security;

import com.iscas.workingdiary.bean.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.ServletException;
import java.util.Date;

public class JWT {
    final static String base64EncodedSecretKey = "0123456789abcdef";  //私钥
    final static long TOKEN_EXPIRATION_TIME = 1000 * 60L; //过期时间

    /**
     * 生成token
     * @param user
     * @return
     */
    public static String getToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserName())
                .claim("userId", user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME)) /*过期时间*/
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey)
                .compact();
    }

    /**
     * 检查token
     * @param token
     * @throws ServletException
     */
    public static Claims checkToken(String token) throws ServletException {
        try {
            return Jwts.parser().setSigningKey(base64EncodedSecretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new ServletException("token expired");
        } catch (Exception e) {
            throw new ServletException("invalid token");
        }
    }
}
