package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.crypto.BitcoinUtils;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.bean.CertInfo;
import com.iscas.workingdiary.bean.Integral;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.config.ConstantProperties;
import com.iscas.workingdiary.mapper.CertMapper;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.service.RepClient;
import com.iscas.workingdiary.service.UserService;
import com.iscas.workingdiary.util.RepChainUtils;
import com.iscas.workingdiary.util.cert.CertUtils;
import com.iscas.workingdiary.util.encrypt.AESCrypt;
import com.iscas.workingdiary.util.encrypt.MD5Utils;
import com.iscas.workingdiary.util.exception.StateCode;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import com.iscas.workingdiary.util.json.ResultData;
import com.sun.org.apache.regexp.internal.RE;
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
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
    private ConstantProperties properties;

    @Autowired
    private UserService userService;

    @Autowired
    private CertService certService;

    @Autowired
    private RepClient repClient;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Cert generateCert(CertInfo certinfo){
        //password 密码用于私钥加密和解密，不做保存，一旦忘记不可找回
        String[] certInfo= {certinfo.getCN(), certinfo.getOU(), certinfo.getO(),
                certinfo.getC(), certinfo.getL(), certinfo.getST()};
        String password = certinfo.getcPassword();

        CertUtils certUtils = new CertUtils();
        Cert cert = new Cert();
        KeyPair keyPair = null;
        X509Certificate certificate = null;
        String pemCert = "";
        String certNo = "";
        String encyptPrivateKey = "";
        try {
            keyPair = certUtils.generateKeyPair();
            certificate = certUtils.generateCert(certInfo, keyPair);    // generate cert
            //addr = BitcoinUtils.calculateBitcoinAddress(certificate.getPublicKey().getEncoded());    //计算证书短地址,避免计算错误不在此计算，认证时由RepChain计算返回
            pemCert = certUtils.getCertPEM(certificate);     // 获取pemcert
            certNo = MD5Utils.stringMD5(pemCert); //使用pemCert的MD5作为证书编号
            encyptPrivateKey = certUtils.encryptPrivateKey(keyPair.getPrivate(), password);    //加密私钥
        }catch (Exception e){
            e.getMessage();
        }

        cert.setCertNo(certNo);
        cert.setPemCert(pemCert);
        cert.setCertLevel("0");
        cert.setCertStatus("0");
        cert.setPrivateKey(encyptPrivateKey);
        cert.setCommonName(certinfo.getCN());
        cert.setCreateTime(new Timestamp(System.currentTimeMillis()));
        try {
            certUtils.generateJksWithCert(certificate, keyPair, password, properties.getJksPath(), certInfo[0]);   //保存jks文件到服务器
            certUtils.saveCertAsPEM(certificate, properties.getCertPath(), certInfo[0]); // 保存cer到服务器
        }catch (Exception e){
            e.printStackTrace();
        }
        return cert;
    }

    /**
     * 注册接口，没有验证用户是否已存在是因为在提交之前已经进行了异步验证
     * @param usercertJson
     * @return
     */
    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(rollbackFor = SQLException.class)
    public ResultData register(@RequestBody JSONObject usercertJson){
        ResultData resultData;
        Cert cert = null;
        User user;
        JSONObject certJson = usercertJson.getJSONObject("certInfo");
        try{
            user = usercertJson.getJSONObject("user").toJavaObject(User.class);
            if (certJson.size()>0){
                cert = generateCert(certJson.toJavaObject(CertInfo.class));
                cert.setUserName(user.getUserName());
                user.setCertNo(cert.getCertNo());
            }
            user.setRegisterTime(new Timestamp(System.currentTimeMillis()));
            userService.userRegister(user, cert);
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
                resultData = new ResultData(StateCode.SUCCESS,  "用户名可用");
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
                resultData = new ResultData(StateCode.SUCCESS,  "ID可用");
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

    /**
     * 修改密码
     * @param request
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @PostMapping(value = "modifypassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData modifyPassword(HttpServletRequest request, @RequestParam String oldPassword, String newPassword){
        ResultData resultData = null;
        String authHead = request.getHeader("Authorization");
        String userName = JWTTokenUtil.parseToken(authHead).getSubject();
        try {
            User user = userService.selectUserByName(userName);
            if (user.getPassword().equals(bCryptPasswordEncoder.encode(oldPassword))){
                userService.modifyPassword(userName, newPassword);
                resultData = new ResultData(StateCode.SUCCESS, "密码修改成功");
            }
        }catch (Exception e){
            resultData = new ResultData(StateCode.DB_ERROR, "系统错误");
        }
        return resultData;
    }


    /**
     * 总积分查询
     * @param
     * @return
     */
    @GetMapping(value = "totalintegral")
    public ResultData queryTotalIntegral(HttpServletRequest request){
        ResultData resultData = null;
        String userName = JWTTokenUtil.parseToken(request.getHeader("Authorization")).getSubject();
        try {
            Integer totalIntegral = userService.queryTotalIntegral(userName);
            if (totalIntegral == null){
                totalIntegral = 0;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("totalIntegral", totalIntegral);
            resultData = new ResultData(StateCode.SUCCESS,"success", jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            resultData = new ResultData(StateCode.DB_ERROR,"查询失败");
        }
        return resultData;
    }

    /**
     * 查询积分详情
     * @return
     */
    @GetMapping(value = "listintegral")
    public List<Integral> seleteIntegralByName(HttpServletRequest request){
        List<Integral> integralList;
        String userName = JWTTokenUtil.parseToken(request.getHeader("Authorization")).getSubject();
        try {
            integralList = userService.queryIntegralList(userName);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return integralList;
    }


    public JSONObject queryIntegralFromRepChain(){
        String userName = "";
        RepChainClient repChainClient = repClient.getRepClient();
        List<String> argsList = repClient.getParamList(userName);
        String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, repClient.getChaincodeId(),"queryIntegral", argsList);
        return repChainClient.postTranByString(hexTransaction);
    }

    /**
     * 注销账户（删除包括证书等所有信息）
     * @param request
     * @return
     */
    @PostMapping(value = "destoryaccount", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData deleteUserById(HttpServletRequest request, HttpServletResponse response, @RequestParam String password){
        ResultData resultData;
        String encrypted = bCryptPasswordEncoder.encode(password);
        String token = request.getHeader("Authorization");
        String userName = JWTTokenUtil.parseToken(token).getSubject();
        try {
            User user = userService.selectUserByName(userName);
            if (user.getPassword().equals(encrypted)){
                userService.destoryAccount(userName);
                resultData = ResultData.deleteSuccess();
            } else {
                resultData = new ResultData(StateCode.DB_DELETE_ERROR, "密码错误");
            }
        } catch (Exception e){
            resultData = new ResultData(StateCode.DB_DELETE_ERROR, "删除失败");
        }

        return resultData;
    }


    /**
     * 退出登录
     */
    public void  logOut(){}

}
