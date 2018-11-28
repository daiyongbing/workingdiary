package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service()
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public void userRegister(User user) {
        userMapper.insertUser(user);
    }

    @Override
    public String userLogin(String userName, String password) {
        return userMapper.userLogin(userName, password);
    }

    @Override
    public void updateById(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public String validate(Integer userId) {
        return userMapper.validate(userId);
    }

    @Override
    public void deleteUserById(int userId) {
        userMapper.deleteUser(userId);
    }

}
