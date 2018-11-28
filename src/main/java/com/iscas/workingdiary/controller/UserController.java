package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.service.UserService;
import com.iscas.workingdiary.util.JsonResultUtils;
import com.iscas.workingdiary.util.ResultData;
import com.iscas.workingdiary.util.encrypt.AESCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public Boolean register(@RequestBody User user){
        String encryptedPWD = AESCrypt.AESEncrypt(user.getPassword());
        user.setPassword(encryptedPWD);
        System.out.println(user.toString());
        userService.userRegister(user);
        return true;
    }

    @PostMapping(value = "validate")
    public Boolean validate(@RequestBody JSONObject jsonObject){
        Integer userId = Integer.parseInt(jsonObject.getString("userId"));
        String result = userService.validate(userId);
        if (result == null) return true;
        else return false;
    }
    // 用户登录
    @PostMapping("login")
    public Integer login(@RequestBody JSONObject object){
        int result = 201;
        String userName = object.getString("userName");
        String password = object.getString("password");
        String cryptpwd = AESCrypt.AESEncrypt(password);
        if (userService.userLogin(userName, cryptpwd) !=null ){
            result = 200;
        }
        return result;
    }

    // 删除用户
    @GetMapping("delete")
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
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
