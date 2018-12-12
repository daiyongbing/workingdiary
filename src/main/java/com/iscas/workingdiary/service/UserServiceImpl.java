package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service()
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void userRegister(User user) {
        // 加密密码
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userMapper.insertUser(user);
    }

    @Override
    public void updateById(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public User findUserByName(String userName) {
        return userMapper.findByUserName(userName);
    }

    @Override
    public User findUserById(Integer userId) {
        return userMapper.findByUserId(userId);
    }

    @Override
    public void deleteUserById(int userId) {
        userMapper.deleteUser(userId);
    }

}
