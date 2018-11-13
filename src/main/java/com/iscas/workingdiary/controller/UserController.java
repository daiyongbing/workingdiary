package com.iscas.workingdiary.controller;

import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户管理类
 */

// @RestController = @Controller + @ResponseBody
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户注册
    @PostMapping(value = "register")
    public User addUser(User user){
        userService.userRegister(user);
        return user;
    }

    // 用户登录
    @PostMapping("login")
    public String login(User user){
        userService.userLogin(user);
       return user.getUserName();
    }

    // 删除用户
    @GetMapping("delete")
    public String deleteUserById(int userId){
        userService.deleteUserById(userId);
        return "OK";
    }

    // 更新用户
    @PostMapping("update")
    public User updateUserById(User user){
        userService.updateById(user);
        return user;
    }
}
