package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.service.UserService;
import com.iscas.workingdiary.util.JsonResultUtils;
import com.iscas.workingdiary.util.ResultData;
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
    public User register(@RequestBody User user){
        /*System.out.println(userJson.toJSONString());
         User user = new User();
         user.setUserName(userJson.getString("userName"));
         user.setUserId(Integer.parseInt(userJson.getString("userId")));
         user.setPassword(userJson.getString("password"));
         user.setProjectTeam(userJson.getString("projectTeam"));
         user.setUserPosition(userJson.getString("userPosition"));*/
        userService.userRegister(user);
        return user;
    }

    // 用户登录
    @PostMapping("login")
    public String login(@RequestBody JSONObject object, HttpServletResponse response, HttpServletRequest request){
        String userName = object.getString("userName");
        String password = object.getString("password");
        if (userService.userLogin(userName, password) !=null ){
            JsonResultUtils.returnJson(request, response, new ResultData());
        }

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
