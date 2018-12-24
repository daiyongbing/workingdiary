package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.bean.CertInfo;
import com.iscas.workingdiary.bean.Integral;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.config.ConstantProperties;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.service.UserService;
import com.iscas.workingdiary.util.repchain.CustomRepChainClient;
import com.iscas.workingdiary.util.repchain.RepChainUtils;
import com.iscas.workingdiary.util.cert.CertUtils;
import com.iscas.workingdiary.util.encrypt.Base64Utils;
import com.iscas.workingdiary.util.encrypt.MD5Utils;
import com.iscas.workingdiary.bean.ResponseStatus;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import com.iscas.workingdiary.util.json.JsonResult;
import com.iscas.workingdiary.util.json.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.sql.Timestamp;
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
            pemCert = Base64Utils.encode2String(certUtils.getPemFromCertificate(certificate));     // 获取pemcert
            certNo = MD5Utils.stringMD5(pemCert); //使用pemCert的MD5作为证书编号
            encyptPrivateKey = certUtils.encryptPrivateKey(keyPair.getPrivate(), password);    //加密私钥
        }catch (Exception e){
            e.getMessage();
        }

        cert.setCertNo(certNo);
        cert.setPemCert(pemCert);
        cert.setCertInfo(Base64Utils.encode2String(JSON.toJSONString(certinfo)));
        cert.setCertLevel("0");
        cert.setCertStatus("0");
        cert.setPrivateKey(encyptPrivateKey);
        cert.setCommonName(certinfo.getCN());
        cert.setCreateTime(new Timestamp(System.currentTimeMillis()));
        try {
            certUtils.generateJksWithCert(certificate, keyPair, password, properties.getJksPath(), certInfo[0]);   //保存jks文件到服务器
            certUtils.savePemCertAsFile(certificate, properties.getCertPath(), certInfo[0]); // 保存cer到服务器
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
            user = usercertJson.getJSONObject("userInfo").toJavaObject(User.class);
            if (certJson.size()>0){
                cert = generateCert(certJson.toJavaObject(CertInfo.class));
                cert.setUserName(user.getUserName());
                user.setCertNo(cert.getCertNo());
            }
            user.setRegisterTime(new Timestamp(System.currentTimeMillis()));
            userService.userRegister(user, cert);
            resultData = new ResultData(ResponseStatus.SUCCESS, "注册成功");
        } catch (DuplicateKeyException de){
            log.error(de.getMessage());
            resultData = new ResultData(ResponseStatus.DB_ALREADY_EXIST_ERROR, "该用户已存在，请勿重复注册");
        } catch (Exception e){
            e.printStackTrace();
            resultData = new ResultData(ResponseStatus.DB_INSERT_ERROR,  "注册失败");
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
                resultData = new ResultData(ResponseStatus.SUCCESS,  "用户名可用");
            } else {
                resultData = new ResultData(ResponseStatus.DB_ALREADY_EXIST_ERROR,  "用户已存在");
            }
        } catch (Exception e){
            e.printStackTrace();
            resultData = new ResultData(ResponseStatus.DB_QUERY_ERROR,  "数据库异常");
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
                resultData = new ResultData(ResponseStatus.SUCCESS,  "ID可用");
            } else {
                resultData = new ResultData(ResponseStatus.DB_ALREADY_EXIST_ERROR,  "用户ID已存在");
            }
        } catch (Exception e){
            e.printStackTrace();
            resultData = new ResultData(ResponseStatus.DB_QUERY_ERROR,  "数据库异常");
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
            resultData = new ResultData(ResponseStatus.DB_UPDATE_ERROR,  "更新失败");
        }
        return resultData;
    }

    /**
     * 修改密码
     * @param request
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "modifypassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData modifyPassword(HttpServletRequest request, @RequestBody JSONObject jsonObject){
        ResultData resultData = null;
        String oldPassword = jsonObject.getString("oldPassword");
        String newPassword = jsonObject.getString("newPassword");
        String authHead = request.getHeader("Authorization");
        String userName = JWTTokenUtil.parseToken(authHead).getSubject();
        try {
            User user = userService.selectUserByName(userName);
            //if (user.getPassword().equals(bCryptPasswordEncoder.encode(oldPassword))){
            if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){
                userService.modifyPassword(userName, newPassword);
                resultData = new ResultData(ResponseStatus.SUCCESS, "密码修改成功");
            } else {
                resultData = new ResultData(ResponseStatus.DB_UPDATE_ERROR, "原密码验证失败");
            }
        }catch (Exception e){
            resultData = new ResultData(ResponseStatus.DB_ERROR, "系统错误");
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
            resultData = new ResultData(ResponseStatus.SUCCESS,"success", jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            resultData = new ResultData(ResponseStatus.DB_ERROR,"查询失败");
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



    /**
     * 注销账户（删除包括证书等所有信息,谨慎操作！！！）
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "destoryaccount", produces = MediaType.APPLICATION_JSON_VALUE)
    public void destoryAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject){
        ResultData resultData;
        String token = request.getHeader("Authorization");
        String userName = JWTTokenUtil.parseToken(token).getSubject();
        String rawPassword = jsonObject.getString("password");
        try {
            User user = userService.selectUserByName(userName);
            if (bCryptPasswordEncoder.matches(rawPassword, user.getPassword())){
                userService.destoryAccount(userName);
                resultData = new ResultData(ResponseStatus.SUCCESS, "删除成功");
            } else {
                resultData = new ResultData(ResponseStatus.DB_DELETE_ERROR, "密码校验失败");
            }
        } catch (Exception e){
            e.printStackTrace();
            resultData = new ResultData(ResponseStatus.DB_DELETE_ERROR, "删除失败");
        }

        JsonResult.resultJson(response, request, resultData);
    }


    /**
     * 退出登录
     */
    public void  logOut(){}



    public JSONObject queryIntegralFromRepChain(){
        String userName = "";
        Certificate certificate = null;
        PrivateKey privateKey = null;
        CustomRepChainClient customRepChainClient = new CustomRepChainClient(properties.getRepchainHost(), certificate, privateKey);
        List<String> argsList = RepChainUtils.getParamList(userName);
        String hexTransaction = RepChainUtils.createHexTransWithListParam(customRepChainClient,"queryIntegral", argsList);
        return customRepChainClient.postTranByString(hexTransaction);
    }

}
