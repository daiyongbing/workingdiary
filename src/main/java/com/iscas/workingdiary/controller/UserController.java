package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.service.UserService;
import com.iscas.workingdiary.util.encrypt.AESCrypt;
import com.iscas.workingdiary.util.exception.StateCode;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import com.iscas.workingdiary.util.json.ResultData;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

/**
 * 用户管理类
 */

// @RestController = @Controller + @ResponseBody
@RestController
@RequestMapping("/user")
public class UserController {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    /**
     * 注册接口，没有验证用户是否已存在是因为在提交之前已经进行了异步验证
     * @param user
     * @return
     */
    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(rollbackFor = SQLException.class)
    public ResultData register(@Valid @RequestBody User user, BindingResult error){
        if (error.hasErrors()){
            System.out.println(error.getAllErrors());
        }
        ResultData resultData = null;
        try{
            userService.userRegister(user);
            resultData = new ResultData(StateCode.SUCCESS, "注册成功");
        } catch (DuplicateKeyException de){
            log.error(de.getMessage());
            resultData = new ResultData(StateCode.DB_ALREADY_EXIST_ERROR, "该用户已存在，请勿重复注册");
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
    @GetMapping(value = "checkname", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData checkUserName(@RequestParam String userName){
        ResultData resultData;
        try {
            User checkUser = userService.selectUserByName(userName);  //验证用户名
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
    @GetMapping(value = "checkid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData checkUserId(@RequestParam String userId){
        ResultData resultData;
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

    /**
     * 用户更新资料（不更新注册时间和密码）
     * @param response
     * @param request
     * @param user
     * @return
     */
    @PostMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData updateUserByName(HttpServletResponse response, HttpServletRequest request, @RequestBody User user){
        ResultData resultData;
        String token = request.getHeader("Authorization");
        String userName;
        try{
            userName = JWTTokenUtil.parseToken(token).getSubject();
            user.setUserName(userName);
            log.info("subject -> "+userName);
            userService.updateByName(user);
            resultData = ResultData.updateSuccess();
        } catch (Exception e){
            e.printStackTrace();
            resultData = new ResultData(StateCode.DB_UPDATE_ERROR,  "更新失败");
        }
        return resultData;
    }




    // 注销账户（删除包括证书等所有信息）
    @GetMapping(value = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData deleteUserById(HttpServletRequest request){
        ResultData resultData = null;
        String userId = request.getParameter("userId");
        try {
            userService.deleteUserById(userId);
            resultData = ResultData.deleteSuccess();
        } catch (Exception e){
            resultData = new ResultData(StateCode.DB_DELETE_ERROR, "删除失败");
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
