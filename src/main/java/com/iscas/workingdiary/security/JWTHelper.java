package com.iscas.workingdiary.security;

import com.iscas.workingdiary.bean.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.ServletException;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;

public class JWTHelper {
    final static String base64SecretKey = "mk4ZjVRiY2Q0NkH25DM3M2NhZGU0ZTgz";  //私钥
    final static long TOKEN_EXPIRATION_TIME = 1000 * 60*60L; //过期时间


    /**
     * 生成token
     * @param user
     * @return
     */
    public static String getToken(User user) {
        String token =  Jwts.builder()
                .setSubject("userInfo")
                .claim("userName", user.getUserName())
                .claim("userId", user.getUserId())
                .claim("role", user.getRoleId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME)) /*过期时间*/
                .signWith(SignatureAlgorithm.HS256, base64SecretKey)
                .compact();

                return token;
    }

    /**
     * 检查token
     * @param token
     * @throws ServletException
     */
    public static Claims checkToken(String token) throws ServletException {
        try {
            return Jwts.parser().setSigningKey(base64SecretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new ServletException("token expired");
        } catch (Exception e) {
            throw new ServletException("invalid token");
        }
    }
}
