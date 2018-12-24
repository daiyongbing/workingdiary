package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.bean.Diary;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.config.ConstantProperties;
import com.iscas.workingdiary.service.AdminService;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.util.Byte2Hex;
import com.iscas.workingdiary.util.cert.CertUtils;
import com.iscas.workingdiary.util.encrypt.Base64Utils;
import com.iscas.workingdiary.util.json.JsonResult;
import com.iscas.workingdiary.util.repchain.CustomRepChainClient;
import com.iscas.workingdiary.util.repchain.RepChainUtils;
import com.iscas.workingdiary.bean.ResponseStatus;
import com.iscas.workingdiary.util.json.ResultData;
import com.protos.Peer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
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
        ResultData resultData = null;
        Cert cert = null;
        JSONObject jsonObject = new JSONObject();
        String certNo = pramJson.getString("certNo");
        try {
            cert = certService.queryCert(certNo);
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
        //反序列化Cert
        Certificate certificate = CertUtils.getCertByPem(Base64Utils.decode2String(cert.getPemCert()));
        // 获得私钥
        PrivateKey privateKey = null;
        try {
            privateKey = CertUtils.decryptPrivateKey(cert.getPrivateKey(), pramJson.getString("password"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        CustomRepChainClient customRepChainClient = new CustomRepChainClient(properties.getRepchainHost(), certificate, privateKey);
        //List<String> argsList = RepChainUtils.getParamList(jsonObject);
        String args = JSONObject.toJSONString(jsonObject);
        String hexTransaction = null;
        Peer.Transaction transaction;
        try {
            transaction = customRepChainClient.createTransWithPK(Peer.Transaction.Type.CHAINCODE_INVOKE, "path",
                    "signCert", args, "string", properties.getChaincodeId(), Peer.ChaincodeSpec.CodeType.CODE_SCALA);
            hexTransaction = Byte2Hex.bytes2hex(transaction.toByteArray());
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject signResult =null ;
        try {
            signResult = customRepChainClient.postTranByString(hexTransaction);
        }catch (Exception e){
            e.printStackTrace();
            resultData = new ResultData(ResponseStatus.REPCHAIN_SERVER_ERROR, "RepChain服务器异常");
        }

       /* String addr = signResult.getString("result");
        cert.setCertAddr(addr);
        cert.setCertStatus("1");
        certService.updateCert(cert);
        resultData = new ResultData(ResponseStatus.SUCCESS, "success");*/
        JsonResult.resultJson(response, request, resultData);
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
            resultData = new ResultData(ResponseStatus.SUCCESS, "密码重置成功");
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
