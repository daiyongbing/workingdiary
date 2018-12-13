package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public interface UserService {

    // register
    void userRegister(User user);

    // delete user
    void deleteUserById(String userId);

    User selectUserByName(String userName);

    User findUserById(String userId);

    void updateByName(User user);
}
