package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.bean.Diary;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.service.AdminService;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.service.RepClient;
import com.iscas.workingdiary.util.repchain.CustomRepChainClient;
import com.iscas.workingdiary.util.repchain.RepChainUtils;
import com.iscas.workingdiary.bean.ResponseStatus;
import com.iscas.workingdiary.util.json.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
     * @param pramJson
     * @return
     */
    @PostMapping(value = "signcert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData signCertByAdmin(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject pramJson){
        ResultData resultData = null;
        Cert cert = null;
        JSONObject jsonObject = new JSONObject();
        String certNO = pramJson.getString("certNO");
        try {
            cert = certService.queryCert(certNO);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (cert == null){
            resultData = new ResultData(ResponseStatus.DB_CERT_NOT_EXIST, "该用户没有上传证书");
        } else {
            String pemCert = cert.getPemCert();
            String certInfo = cert.getCertInfo();
            jsonObject.put(pemCert, certInfo);
        }
        RepChainClient repChainClient = repClient.getRepClient();
        CustomRepChainClient customRepChainClient = repClient.getCustomRepClient(null, null);
        List<String> argsList = repClient.getParamList(jsonObject);
        String hexTransaction = RepChainUtils.createHexTransaction(customRepChainClient, repClient.getChaincodeId(),"certProof", argsList);
        JSONObject signResult = repChainClient.postTranByString(hexTransaction);

        String addr = signResult.getString("result");
        cert.setCertAddr(addr);
        cert.setCertStatus("1");
        certService.updateCert(cert);
        resultData = new ResultData(ResponseStatus.SUCCESS, "success");
        return resultData;
    }

    /**
     * 查看用户列表(分页查询所有用户)
     * @return
     */
    @GetMapping(value = "userlist")
    public List<User> selectAll(){
        List<User> userList = null;
        try {
            userList = adminService.selectAllUser();
        } catch (Exception e){
            e.printStackTrace();
            new Exception("系统错误");
        }
        return userList;
    }

    /**
     * 重置用户密码
     * @return
     */
    @PostMapping(value = "resetpwd" )
    public ResultData resetPassword(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject userNameJson){
        ResultData resultData = null;
        String userName = userNameJson.getString("userName");
        try {
            adminService.resetPassword(userName);
            resultData = ResultData.updateSuccess();
        } catch (Exception e){
            e.printStackTrace();
            resultData = new ResultData(ResponseStatus.DB_UPDATE_ERROR, "系统错误");
        }
        return resultData;
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
