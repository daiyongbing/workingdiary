package com.iscas.workingdiary.controller;

import com.iscas.workingdiary.bean.Admin;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// @RestController = @Controller + @ResponseBody
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping(value = "adduser")
    public Object addUser(String username, String password){
        Admin user = new Admin();
        user.setUsername(username);
        user.setPassword(password);
        userService.addUser(user);
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
