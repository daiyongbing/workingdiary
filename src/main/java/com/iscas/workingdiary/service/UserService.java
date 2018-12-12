package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public interface UserService {

    // register
    void userRegister(User user);

    // delete user
    void deleteUserById(int userId);

    // update user
    void updateById(User user);

    User findUserByName(String userName);

    User findUserById(Integer userId);
}
