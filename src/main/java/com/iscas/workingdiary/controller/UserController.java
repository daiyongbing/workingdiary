package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.jwtsecurity.JWTHelper;
import com.iscas.workingdiary.service.UserService;
import com.iscas.workingdiary.util.encrypt.AESCrypt;
import com.iscas.workingdiary.util.exception.StateCode;
import com.iscas.workingdiary.util.json.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

/**
 * 用户管理类
 */

// @RestController = @Controller + @ResponseBody
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册接口，没有验证用户是否已存在是因为在提交之前已经进行了异步验证
     * @param user
     * @return
     */
    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(rollbackFor = SQLException.class)
    public ResultData register(@RequestBody User user){
        ResultData resultData = null;
        String encryptedPWD = AESCrypt.AESEncrypt(user.getPassword());
        user.setPassword(encryptedPWD);
        try{
            userService.userRegister(user);
            resultData = new ResultData(StateCode.SUCCESS, "注册成功");
        } catch (Exception e){
            e.printStackTrace();
            resultData = new ResultData(StateCode.DB_INSERT_ERROR,  "注册失败");
        }
        return resultData;
    }

    /**
     * 异步验证
     * @param user
     * @return
     */
    @GetMapping(value = "validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData validate(@RequestBody User user){
        ResultData resultData = null;
        try {
            List<User> checkUser = userService.validate(user);  //验证用户名和ID
            if (checkUser == null){
                resultData = new ResultData(StateCode.SUCCESS,  "验证成功");
            } else {
                resultData = new ResultData(StateCode.DB_ALREADY_EXIST_ERROR,  "用户已存在");
            }
        } catch (Exception e){
            e.printStackTrace();
            resultData = new ResultData(StateCode.DB_QUERY_ERROR,  "数据库异常");
        }
        return resultData;
    }

    // 用户登录
    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData login(HttpServletResponse response, @RequestBody JSONObject object){
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
            String token = JWTHelper.getToken(user);
            response.addHeader("token", token);
            resultData = new ResultData(StateCode.SUCCESS,  "登录成功", user);
        } else {
            resultData = new ResultData(StateCode.DB_QUERY_NULL_ERROR,  "账户或密码错误");
        }
        return resultData;
    }

    // 删除用户
    @GetMapping(value = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public ResultData deleteUserById(HttpServletRequest request){
        ResultData resultData = null;
        String token = request.getHeader("Authorization").substring(7);
        try {
            Integer userId = JWTHelper.checkToken(token).get("userId", Integer.class);
            userService.deleteUserById(userId);
            resultData = ResultData.deleteSuccess();
        } catch (Exception e){
            resultData = new ResultData(StateCode.DB_DELETE_ERROR, "删除失败");
        }

        return resultData;
    }

    // 更新用户
    @PostMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData updateUserById(HttpServletResponse response, HttpServletRequest request, @RequestBody User user){
        ResultData resultData = null;
        String encryptedPWD = AESCrypt.AESEncrypt(user.getPassword());
        user.setPassword(encryptedPWD);
        String token = request.getHeader("Authorization");
        String userId;
        try{
            userId = JWTHelper.checkToken(token).getId();
            System.out.println(userId);
            userService.updateById(user);
            resultData = ResultData.updateSuccess();
        } catch (Exception e){
            resultData = new ResultData(StateCode.DB_UPDATE_ERROR,  "更新失败");
        }
        return resultData;
    }

    /**
     * 注销账户
     */
    public void  logOut(){}

    /**
     * 退出登录
     */
    public void logOff(){}
}
