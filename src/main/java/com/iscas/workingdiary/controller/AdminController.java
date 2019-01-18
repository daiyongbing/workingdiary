package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.bean.Diary;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.config.ConstantProperties;
import com.iscas.workingdiary.service.AdminService;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.service.UserService;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import com.iscas.workingdiary.util.json.JsonResult;
import com.iscas.workingdiary.util.repchain.CustomRepChainClient;
import com.iscas.workingdiary.bean.ResponseStatus;
import com.iscas.workingdiary.util.json.ResultData;
import com.iscas.workingdiary.util.repchain.TransactionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 所有接口必须要有管理员权限
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private CertService certService;

    @Autowired
    private ConstantProperties properties;

    /**
     * 证书入链
     * @param pramJson
     * @return
     */
    @PostMapping(value = "signcert", produces = MediaType.APPLICATION_JSON_VALUE)
    public void signCertByAdmin(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject pramJson){
        String authHeader = request.getHeader("Authorization");
        Cert userCert = null;
        Cert adminCert = null;
        JSONObject jsonObject = new JSONObject();
        String certNo = pramJson.getString("certNo");
        String adminName = JWTTokenUtil.parseToken(authHeader).getSubject();
        try {
            userCert = certService.queryCert(certNo);
            adminCert = certService.getCertByName(adminName);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (userCert == null){
            JsonResult.resultJson(response, request, ResponseStatus.DB_CERT_NOT_EXIST, "该用户没有上传证书");
            return;
        } else {
            String pemCert = userCert.getPemCert();
            String certInfo = userCert.getCertInfo();
            jsonObject.put("userName",userCert.getUserName());
            jsonObject.put("pemCert", pemCert);
            jsonObject.put("certInfo", certInfo);
        }
        String hexTransaction = TransactionUtils.register(adminCert, jsonObject, pramJson.getString("password"));
        JSONObject signResult =null ;
        CustomRepChainClient customRepChainClient = new CustomRepChainClient(properties.getRepchainHost());
        try {
            signResult = customRepChainClient.postTransByString(JSON.toJSONString(hexTransaction));
        }catch (IOException e){
            JsonResult.resultJson(response, request,ResponseStatus.REPCHAIN_SERVER_CONNECTION_ERROR,  "RepChain服务器连接失败");
            return;
        } catch (RuntimeException e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.REPCHAIN_SERVER_ERROR, "RepChain服务器异常");
            return;
        }
        System.out.println(signResult);
        System.out.println("err:"+signResult.getJSONObject("result").getString("err"));
       if (signResult.getString("code") == "200" && signResult.getJSONObject("result").getString("err") == null){
           String addr = signResult.getString("result");
           userCert.setCertAddr(addr);
           userCert.setCertStatus("1");
           certService.updateCert(userCert);
           JsonResult.resultJson(response, request,ResponseStatus.SUCCESS, signResult);
       } else {
           JsonResult.resultJson(response, request,ResponseStatus.REPCHAIN_REQUEST_ERROR, signResult); ;
       }
    }


    /**
     * 查看用户列表（数据库）(分页查询所有用户)
     * @return
     */
    @GetMapping(value = "userlist")
    public /*List<User>*/void selectAllByPage(HttpServletResponse response, HttpServletRequest request, @RequestParam int currentPage, int pageSize){
        List<User> userList = null;
        try {
            //userList = adminService.selectAllUser();
            userList = userService.queryUsersByPage(currentPage, pageSize);
            JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData(userList));
        } catch (Exception e){
            e.printStackTrace();
            new Exception("系统错误");
        }
        //return userList;
    }

    /**
     * 重置用户密码
     * @return
     */
    @PostMapping(value = "resetpwd" )
    public void resetPassword(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject userNameJson){
        ResultData resultData = null;
        String userName = userNameJson.getString("userName");
        try {
            adminService.resetPassword(userName);
            JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("密码重置成功"));
        } catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("服务器异常"));
        }
    }

    /**
     * 查询用户日志
     * @return
     */
    @GetMapping(value = "userdiary")
    public List<Diary> selectDiaryByName(HttpServletRequest request, HttpServletResponse response, @RequestParam String userName){
        List<Diary> diaryList = null;
        try {
            diaryList = adminService.selectDiaryByName(userName);
        }catch (Exception e){
            e.printStackTrace();
            new Exception("系统错误");
        }
        return diaryList;
    }
}
