package com.iscas.workingdiary.service.impl;

import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.mapper.IUserMapper;
import com.iscas.workingdiary.service.IUserService;

public class UserServiceImpl implements IUserService {

    private IUserMapper userMapper;

    @Override
    public User login(User user) {
        return userMapper.login(user);
    }
}
