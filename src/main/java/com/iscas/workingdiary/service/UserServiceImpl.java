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
    public User selectUserByName(String userName) {
        return userMapper.selectByUserName(userName);
    }

    @Override
    public User findUserById(String userId) {
        return userMapper.findByUserId(userId);
    }

    @Override
    public void updateByName(User user) {
        userMapper.updateByName(user);
    }

    @Override
    public void deleteUserById(String userId) {
        userMapper.deleteUser(userId);
    }

}
