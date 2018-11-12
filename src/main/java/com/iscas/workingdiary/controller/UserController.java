package com.iscas.workingdiary.controller;

import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.service.UserService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理类
 */

// @RestController = @Controller + @ResponseBody
@RestController
@RequestMapping("/user")
@EnableAutoConfiguration
public class UserController {

    //@Autowired
    private UserService userService;

    // 用户注册
    @PostMapping(value = "register")
    public Object addUser(User user){
        System.out.println(user.toString());
        userService.userRegister(user);

        return user;
    }

    // 用户登录
    @PostMapping("login")
    public String login(User user){
        userService.login(user);
       return user.getUserName();
    }

    // 删除用户
    @GetMapping("delete")
    public String deleteUserById(int userId){
        userService.deleteById(userId);
        return "OK";
    }

    // 更新用户
    @PostMapping("update")
    public User updateUserById(User user){
        System.out.println(user.toString());
        userService.updateById(user);
        return user;
    }
}
