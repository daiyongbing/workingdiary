package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service()
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public void userRegister(User user) {
        userMapper.insertUser(user);
    }

    @Override
    public User userLogin(String userName, String password) {
        return userMapper.userLogin(userName, password);
    }

    @Override
    public void updateById(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public List<User> validate(User user) {
        return userMapper.validate(user);
    }

    @Override
    public void deleteUserById(int userId) {
        userMapper.deleteUser(userId);
    }

}
