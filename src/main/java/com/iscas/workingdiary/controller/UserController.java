package com.iscas.workingdiary.controller;

import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.service.IUserService;
import com.iscas.workingdiary.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// @RestController = @Controller + @ResponseBody
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    public IUserService userService;

    @GetMapping(value = "adduser")
    public Object addUser(String username, String password){
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        return user;
    }
    @GetMapping(value = "saveuser")
    public Object saveUser(@RequestBody User user){
        return user;
    }


    @PostMapping("login")
    public Object login(String username, String password){
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        User result = userService.login(user);
        return result;
    }
}
