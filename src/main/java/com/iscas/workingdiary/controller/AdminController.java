package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.bean.Diary;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.service.AdminService;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.service.RepClient;
import com.iscas.workingdiary.util.RepChainUtils;
import com.iscas.workingdiary.util.encrypt.Base64Utils;
import com.iscas.workingdiary.util.exception.StateCode;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import com.iscas.workingdiary.util.json.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 所有接口必须要有管理员权限
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    @Autowired
    private RepClient repClient;

    @Autowired
    private AdminService adminService;

    @Autowired
    private CertService certService;


    /**
     * 证书入链
     * @param object
     * @return
     */
    @PostMapping(value = "signcert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData signCertByAdmin(@RequestBody JSONObject object){
        ResultData resultData = null;
        Integer userId = object.getInteger("userId");
        String userInfo = object.getJSONObject("userInfo").toJSONString();
        String base64Info = Base64Utils.encode2String(userInfo);
        Cert cert = null;
        try {
            cert = certService.verifyCert(userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        if (cert == null){
            resultData = new ResultData(StateCode.DB_CERT_NOT_EXIST, "该用户没有上传证书");
        } else {
            String pemCert = cert.getPemCert();
            jsonObject.put(pemCert, base64Info);
        }
        RepChainClient repChainClient = repClient.getRepClient();
        List<String> argsList = repClient.getParamList(jsonObject);
        String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, repClient.getChaincodeId(),"certProof", argsList);
        JSONObject signResult = repChainClient.postTranByString(hexTransaction);

        resultData = new ResultData(StateCode.SUCCESS, "success", signResult.getString("err"));

        return resultData;
    }

    /**
     * 查看用户列表
     * @return
     */
    @GetMapping(value = "userlist")
    public List<User> selectAll(){
        List<User> userList = null;
        return userList;
    }

    /**
     * 重置用户密码
     * @return
     */
    @PostMapping(value = "resetpwd" )
    public ResultData resetPassword(){
        ResultData resultData = null;

        return resultData;
    }

    /**
     * 查询用户日志
     * @return
     */
    @PostMapping(value = "userdiary")
    public List<Diary> selectDiary(){
        return new ArrayList<>();
    }
}
