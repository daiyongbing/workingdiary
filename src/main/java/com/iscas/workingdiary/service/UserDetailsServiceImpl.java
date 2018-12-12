package com.iscas.workingdiary.service;

import com.iscas.workingdiary.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.iscas.workingdiary.bean.User user = userMapper.findByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        String password = user.getPassword();
        log.info("用户名:"+username+password);
        return new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

}
