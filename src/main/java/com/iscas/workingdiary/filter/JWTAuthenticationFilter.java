package com.iscas.workingdiary.filter;

import com.iscas.workingdiary.exception.handler.JWTTokenExceptionHandler;
import com.iscas.workingdiary.service.CustomUserDetailsService;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 实现token的校验功能，特别注意，当Authorization为null时一定要放行，否则所有的接口都会被拦截下来导致访问任何接口都需要token
 */

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.info("JWTAuthenticationFilter -> doFilterInternal");
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null){
            if (!authHeader.startsWith("Bearer ")) {
                JWTTokenExceptionHandler.invalidAuthFormatException(response);
                return;
            }
            try {
                String username = JWTTokenUtil.parseToken(authHeader).getSubject();
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (ExpiredJwtException e) {
                JWTTokenExceptionHandler.expiredAuthorizationException(response);
                return;
            } catch (SignatureException e){
                JWTTokenExceptionHandler.invalidAuthorizationException(response);
            } catch (MalformedJwtException e){
                JWTTokenExceptionHandler.invalidAuthorizationException(response);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
