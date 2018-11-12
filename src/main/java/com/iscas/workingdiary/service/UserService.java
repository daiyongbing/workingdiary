package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.User;

public interface UserService {

    // register
    void userRegister(User user);

    //login in
    String login(User user);

    // delete user
    void deleteById(int userId);

    // update user

    void updateById(User user);
}
