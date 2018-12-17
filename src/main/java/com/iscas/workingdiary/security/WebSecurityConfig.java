package com.iscas.workingdiary.security;

import com.iscas.workingdiary.filter.JWTAuthenticationFilter;
import com.iscas.workingdiary.filter.JWTLoginFilter;
import com.iscas.workingdiary.security.handler.*;
import com.iscas.workingdiary.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomAuthenticationEntryPoint authenticationEntryPoint;  //  未登陆时返回 JSON 格式的数据给前端（否则为 html）

    @Autowired
    CustomAuthenticationSuccessHandler authenticationSuccessHandler;  // 登录成功返回的 JSON 格式数据给前端（否则为 html）

    @Autowired
    CustomAuthenticationFailureHandler authenticationFailureHandler;  //  登录失败返回的 JSON 格式数据给前端（否则为 html）

    @Autowired
    CustomLogoutSuccessHandler logoutSuccessHandler;  // 注销成功返回的 JSON 格式数据给前端（否则为 登录时的 html）

    @Autowired
    CustomAccessDeniedHandler accessDeniedHandler;    // 无权访问返回的 JSON 格式数据给前端（否则为 403 html 页面）

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    JWTLoginFilter jwtLoginFilter(AuthenticationManager authenticationManager){
        return new JWTLoginFilter(authenticationManager);
    }

    @Autowired
    JWTAuthenticationFilter jwtAuthenticationTokenFilter; // JWT 拦截器

   /* public WebSecurityConfig(CustomUserDetailsService userDetailService, BCryptPasswordEncoder cryptPasswordEncoder) {
        this.userDetailsService = userDetailService;
        this.bCryptPasswordEncoder = cryptPasswordEncoder;
    }*/

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 去掉 CSRF
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 使用 JWT，关闭token
                .and()

                .httpBasic().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/register").permitAll() //开放注册接口和验证接口
                .antMatchers(HttpMethod.GET, "/user/checkname","/user/checkid", "/swagger-ui.html").permitAll() //开发验证接口
                .antMatchers("/admin").hasRole("ADMIN") // 只有管理员能访问/admin/**
                .anyRequest().authenticated() //所有接口都必须经过身份验证
                .and()

                .authorizeRequests()

                .anyRequest()
                .access("@rbacauthorityservice.hasPermission(request,authentication)") // RBAC 动态 url 认证

               /*
               // 表单登录时启用，REST api忽略此项
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)*/

                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtLoginFilter(authenticationManager()))
                .csrf().disable();
    }
}
