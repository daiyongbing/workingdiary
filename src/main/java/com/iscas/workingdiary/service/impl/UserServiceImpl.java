package com.iscas.workingdiary.service.impl;

import com.iscas.workingdiary.bean.Admin;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.mapper.IUserMapper;
import com.iscas.workingdiary.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper userMapper;

    @Override
    public Admin addUser(Admin admin) {
        userMapper.insertAdim(admin);
        return admin;
    }

    @Override
    public User login(User user) {
        return userMapper.login(user);
    }
}
