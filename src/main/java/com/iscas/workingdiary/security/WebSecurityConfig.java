package com.iscas.workingdiary.security;

import com.iscas.workingdiary.filter.JWTAuthenticationFilter;
import com.iscas.workingdiary.filter.JWTLoginFilter;
import com.iscas.workingdiary.service.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private CustomUserDetailsService userDetailsService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurityConfig(CustomUserDetailsService userDetailService, BCryptPasswordEncoder cryptPasswordEncoder) {
        this.userDetailsService = userDetailService;
        this.bCryptPasswordEncoder = cryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/register").permitAll() //开放注册接口和验证接口
                .antMatchers(HttpMethod.GET, "/user/checkname","/user/checkid").permitAll() //开发验证接口
                .anyRequest().authenticated() //所有接口都必须经过身份验证

                .antMatchers("/admin").hasRole("ADMIN") // 只有管理员能访问/admin/**

                .and()
                .addFilter(new JWTLoginFilter(authenticationManager()))
                .addFilter(new JWTAuthenticationFilter(authenticationManager()));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
