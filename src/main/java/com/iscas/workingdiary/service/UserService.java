package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public interface UserService {

    // register
    void userRegister(User user);

    //login in
    User userLogin(String userName, String password);

    // delete user
    void deleteUserById(int userId);

    // update user
    void updateById(User user);

    List<User> validate(User user);
}
