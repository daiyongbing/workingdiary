package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.service.UserService;
import com.iscas.workingdiary.util.encrypt.AESCrypt;
import com.iscas.workingdiary.util.exception.StateCode;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import com.iscas.workingdiary.util.json.ResultData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
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
     * 验证用户名是否存在
     * @param userName
     * @return
     */
    @GetMapping(value = "existname", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData validate(@RequestParam String userName){
        ResultData resultData = null;
        try {
            User checkUser = userService.findUserByName(userName);  //验证用户名
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

    /**
     * 验证用户ID是否存在
     * @param userId
     * @return
     */
    @GetMapping(value = "existid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData validate(@RequestParam Integer userId){
        ResultData resultData = null;
        try {
            User checkUser = userService.findUserById(userId);  //验证用户ID
            if (checkUser == null){
                resultData = new ResultData(StateCode.SUCCESS,  "验证成功");
            } else {
                resultData = new ResultData(StateCode.DB_ALREADY_EXIST_ERROR,  "用户ID已存在");
            }
        } catch (Exception e){
            e.printStackTrace();
            resultData = new ResultData(StateCode.DB_QUERY_ERROR,  "数据库异常");
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
            Integer userId = JWTTokenUtil.parseToken(token).get("userId", Integer.class);
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
        Integer userId;
        try{
            userId = JWTTokenUtil.parseToken(token).get("userId", Integer.class);
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
