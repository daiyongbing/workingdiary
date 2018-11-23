package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.User;
import org.springframework.stereotype.Service;

@Service()
public interface UserService {

    // register
    void userRegister(User user);

    //login in
    String userLogin(String userName, String password);

    // delete user
    void deleteUserById(int userId);

    // update user

    void updateById(User user);
}
