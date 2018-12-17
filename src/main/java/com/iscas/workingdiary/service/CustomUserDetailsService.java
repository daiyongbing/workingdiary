package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.CustomUserDetails;
import com.iscas.workingdiary.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;

/**
 * 登录验证
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      /*  CustomUserDetails userInfo = new CustomUserDetails();
        userInfo.setUsername(username);
        userInfo.setPassword(new BCryptPasswordEncoder().encode("123"));

        Set authoritiesSet = new HashSet();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        authoritiesSet.add(authority);
        userInfo.setAuthorities(authoritiesSet);
        return userInfo;*/

        log.info("loadUserByUsername -> username:"+username);
        if (username == null || username == ""){
            return null;
        }
        CustomUserDetails userDetails = userMapper.findByUserName(username);
        if(userDetails == null){
            throw new UsernameNotFoundException(username);
        }
        String password = userDetails.getPassword();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        log.info(username+":"+authorities);
        //return new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList(userDetails.getAuthorities()));
        return new User(username, password, authorities);
    }

}
