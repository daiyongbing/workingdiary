package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.iscas.workingdiary.bean.Cert;
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
            //Integer role = JWTTokenUtil.parseToken(token).get("role", Integer.class);
            Integer role = 0;
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
            //Integer role = JWTTokenUtil.parseToken(token).get("role", Integer.class);
            Integer role = 0;
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

    /**
     * 证书入链
     * @param object
     * @return
     */
    @PostMapping(value = "sign", produces = MediaType.APPLICATION_JSON_VALUE)
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
}
