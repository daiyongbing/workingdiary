package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public void userRegister(User user) {

    }

    @Override
    public String login(User user) {
        return null;
    }

    @Override
    public void deleteById(int userId) {

    }

    @Override
    public void updateById(User user) {
        userMapper.updateById(user);
    }
}
