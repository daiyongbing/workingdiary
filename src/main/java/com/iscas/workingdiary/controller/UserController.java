package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.service.UserService;
import com.iscas.workingdiary.util.encrypt.AESCrypt;
import com.iscas.workingdiary.util.exception.StateCode;
import com.iscas.workingdiary.util.json.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public ResultData register(@RequestBody User user){
        ResultData resultData = null;
        String encryptedPWD = AESCrypt.AESEncrypt(user.getPassword());
        user.setPassword(encryptedPWD);
        try{
            userService.userRegister(user);
            resultData = new ResultData(StateCode.SUCCESS, "插入成功");
        } catch (Exception e){
            resultData = new ResultData(StateCode.DB_INSERT_ERROR,  "插入失败");
        }
        return resultData;
    }

    @GetMapping(value = "validate")
    public ResultData validate(@RequestParam("userId") Integer userId){
        ResultData resultData = null;
        String result = null;
        try {
            result = userService.validate(userId);
        } catch (Exception e){
            resultData = new ResultData(StateCode.DB_QUERY_ERROR,  "数据库查询异常");
        }

        if (result == null){
            resultData = new ResultData(StateCode.DB_QUERY_NULL_ERROR,  "用户不存在");
        } else {
            resultData = new ResultData(StateCode.DB_ALREADY_EXIST_ERROR,  "用户已存在");
        }
        return resultData;
    }

    // 用户登录
    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData login(@RequestBody JSONObject object){
        String userName = object.getString("userName");
        String password = object.getString("password");
        String cryptpwd = AESCrypt.AESEncrypt(password);
        User user = null;
        ResultData resultData = null;
        try {
            user = userService.userLogin(userName, cryptpwd);
        } catch (Exception e){
            resultData = new ResultData(StateCode.DB_ERROR,  "数据库异常");
        }
        if ( user !=null ){
            resultData = new ResultData(StateCode.SUCCESS,  "登录成功", user);
        } else {
            resultData = new ResultData(StateCode.DB_QUERY_NULL_ERROR,  "账户或密码错误");
        }
        return resultData;
    }

    // 删除用户
    @GetMapping("delete")
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public ResultData deleteUserById(@RequestParam("userId") Integer userId){
        ResultData resultData = null;
        try {
            userService.deleteUserById(userId);
            resultData = ResultData.deleteSuccess();
        } catch (Exception e){
            resultData = new ResultData(StateCode.DB_DELETE_ERROR, "删除失败");
        }

        return resultData;
    }

    // 更新用户
    @PostMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData updateUserById(@RequestBody User user){
        ResultData resultData = null;
        String encryptedPWD = AESCrypt.AESEncrypt(user.getPassword());
        user.setPassword(encryptedPWD);
        try{
            userService.updateById(user);
            resultData = ResultData.updateSuccess();
        } catch (Exception e){
            resultData = new ResultData(StateCode.DB_UPDATE_ERROR,  "更新失败");
        }
        return resultData;
    }
}
