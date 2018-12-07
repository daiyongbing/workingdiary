package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.security.JWTHelper;
import com.iscas.workingdiary.service.AdminService;
import com.iscas.workingdiary.util.exception.StateCode;
import com.iscas.workingdiary.util.json.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * 所有接口必须要有管理员权限
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 删除用户
     * @param request
     * @return
     */
    @PostMapping(value = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(rollbackFor = SQLException.class)
    public ResultData deleteUserById(HttpServletRequest request, @RequestBody JSONObject jsonObject){
        ResultData resultData = null;
        String token = request.getHeader("Authorization").substring(7);
        String userName = jsonObject.getString("userName");
        Integer userId = jsonObject.getInteger("userId");
        try {
            Integer role = JWTHelper.checkToken(token).get("role", Integer.class);
            if (role == 1){
                adminService.deleteUser(userName, userId);
                resultData = ResultData.deleteSuccess();
            } else {
                resultData = new ResultData(StateCode.SERVER_NO_ACCESS_ERROR, "权限不足，请登录管理员账户操作");
            }
        } catch (Exception e){
            resultData = new ResultData(StateCode.DB_DELETE_ERROR, "删除失败");
        }
        return resultData;
    }

    /**
     * 更新用户信息
     * @param
     * @param request
     * @param user
     * @return
     */
    @PostMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData updateUserById(HttpServletRequest request, @RequestBody User user){
        ResultData resultData = null;
        String token = request.getHeader("Authorization").substring(7);
        try{
            Integer role = JWTHelper.checkToken(token).get("role", Integer.class);
            if (role == 1){
                adminService.updateUser(user);
                resultData = ResultData.updateSuccess();
            } else {
                resultData = new ResultData(StateCode.SERVER_NO_ACCESS_ERROR, "权限不足，请登录管理员账户操作");
            }
        } catch (Exception e){
            resultData = new ResultData(StateCode.DB_UPDATE_ERROR,  "更新失败");
        }
        return resultData;
    }
}
