package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiary.bean.*;
import com.iscas.workingdiary.bean.ResponseStatus;
import com.iscas.workingdiary.config.ConstantProperties;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.service.UserService;
import com.iscas.workingdiary.util.cert.CertUtils;
import com.iscas.workingdiary.util.repchain.CustomRepChainClient;
import com.iscas.workingdiary.util.cert.CertificateUtils;
import com.iscas.workingdiary.util.encrypt.Base64Utils;
import com.iscas.workingdiary.util.encrypt.MD5Utils;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import com.iscas.workingdiary.util.json.JsonResult;
import com.iscas.workingdiary.util.json.ResultData;
import com.iscas.workingdiary.util.repchain.TransactionUtils;
import org.apache.ibatis.annotations.Param;
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
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
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

    /**
     * 验证用户名是否存在
     * @param response
     * @param request
     * @param userName
     */
    @GetMapping(value = "checkname", produces = MediaType.APPLICATION_JSON_VALUE)
    public void checkUserName(HttpServletResponse response, HttpServletRequest request, @RequestParam String userName){
        try {
            User checkUser = userService.selectUserByName(userName);  //验证用户名
            if (checkUser == null){
                JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("用户名可用"));
            } else {
                JsonResult.resultJson(response, request, ResponseStatus.DB_ALREADY_EXIST_ERROR,  new ResultData("用户已存在"));
            }
        } catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR,  new ResultData("服务器异常"));
        }
    }

    /**
     * 验证用户ID是否存在
     * @param userId
     * @return
     */
    @GetMapping(value = "checkid", produces = MediaType.APPLICATION_JSON_VALUE)
    public void checkUserId(HttpServletResponse response, HttpServletRequest request, @RequestParam String userId){
        try {
            User checkUser = userService.findUserById(userId);  //验证用户ID
            if (checkUser == null){
                JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("ID可用"));
            } else {
                JsonResult.resultJson(response, request, ResponseStatus.DB_ALREADY_EXIST_ERROR,  new ResultData("ID已存在"));
            }
        } catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR,  new ResultData("服务器异常"));
        }
    }

    /**
     * 注册接口，没有验证用户是否已存在是因为在提交之前已经进行了异步验证
     * @param usercertJson
     * @return
     */
    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(rollbackFor = SQLException.class)
    public void register(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject usercertJson){
        Cert cert = null;
        User user;
        JSONObject certJson = usercertJson.getJSONObject("certInfo");
        try{
            user = usercertJson.getJSONObject("userInfo").toJavaObject(User.class);
            if (certJson.size()>0){
                cert = CertUtils.genCert(certJson.toJavaObject(CertInfo.class), properties.getJksPath(), properties.getCertPath());
                cert.setUserName(user.getUserName());
                user.setCertNo(cert.getCertNo());
            }
            user.setRegisterTime(new Timestamp(System.currentTimeMillis()));
            userService.userRegister(user, cert);
            JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("注册成功"));
        } catch (DuplicateKeyException de){
            log.error(de.getMessage());
            JsonResult.resultJson(response, request, ResponseStatus.DB_ALREADY_EXIST_ERROR, new ResultData("该用户已存在，请勿重复注册"));
            return;
        } catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("服务器异常"));
        }
    }


    /**
     * 用户更新资料（不更新注册时间和密码）
     * @param response
     * @param request
     * @param user
     * @return
     */
    @PostMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateUserByName(HttpServletResponse response, HttpServletRequest request, @RequestBody User user){
        ResultData resultData;
        String token = request.getHeader("Authorization");
        String userName;
        try{
            userName = JWTTokenUtil.parseToken(token).getSubject();
            user.setUserName(userName);
            log.info("subject -> "+userName);
            userService.updateByName(user);
            JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("更新成功"));
        } catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("服务器异常"));
        }
    }

    /**
     * 修改密码
     * @param request
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "modifypassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData modifyPassword(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject){
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
                JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("密码修改成功"));
            } else {
                JsonResult.resultJson(response, request, ResponseStatus.DB_UPDATE_ERROR, new ResultData("原密码验证失败"));
            }
        }catch (Exception e){
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("服务器错误"));
        }
        return resultData;
    }


    /**
     * 总积分查询（本地数据库）
     * @param
     * @return
     */
    @GetMapping(value = "mycredit")
    public ResultData queryTotalIntegral(HttpServletRequest request, HttpServletResponse response){
        ResultData resultData = null;
        String userName = JWTTokenUtil.parseToken(request.getHeader("Authorization")).getSubject();
        try {
            Integer totalIntegral = userService.queryTotalIntegral(userName);
            if (totalIntegral == null){
                totalIntegral = 0;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("credit", totalIntegral);
            JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("success", jsonObject));
        }catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("服务器异常"));
        }
        return resultData;
    }

    /**
     * 查询积分详情(本地数据库)
     * @return
     */
    @GetMapping(value = "creditlist")
    public List<Integral> seleteIntegralByName(HttpServletRequest request){
        List<Integral> creditlist;
        String userName = JWTTokenUtil.parseToken(request.getHeader("Authorization")).getSubject();
        try {
            creditlist = userService.queryIntegralList(userName);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return creditlist;
    }



    /**
     * 注销账户（删除包括证书等所有信息,谨慎操作！！！）
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "destoryaccount", produces = MediaType.APPLICATION_JSON_VALUE)
    public void destoryAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject){
        String token = request.getHeader("Authorization");
        String userName = JWTTokenUtil.parseToken(token).getSubject();
        String rawPassword = jsonObject.getString("password");
        try {
            User user = userService.selectUserByName(userName);
            if (bCryptPasswordEncoder.matches(rawPassword, user.getPassword())){
                userService.destoryAccount(userName);
                JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("注销成功"));
            } else {
                JsonResult.resultJson(response, request, ResponseStatus.DB_DELETE_ERROR, new ResultData("密码校验失败"));
            }
        } catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("服务器异常"));
        }
    }


    /**
     * 退出登录
     */
    public void  logOut(){}

    /**
     * 上传日志（同时上传到本地数据库和区块链）
     * @param jsonLog
     * @return
     */
    @PostMapping(value = "pushdairy", produces = "application/json;charset=UTF-8")
    public void postWorkingDairyWithSign(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonLog){
        Cert cert = getCert(request);
        // 证书异常，终止上链请求
        if (cert == null){
            JsonResult.resultJson(response, request, ResponseStatus.DB_CERT_NOT_EXIST, new ResultData("没有证书"));
            return;
        }
        if ("0".equals(cert.getCertStatus())){
            JsonResult.resultJson(response, request, ResponseStatus.DB_CERT_NOT_PROOF, new ResultData("证书未认证"));
            return;
        }
        // 正常可用，构建参数提交给区块链，处理异常
        String text = jsonLog.getJSONObject("diary").toJSONString();
        String jks_password = jsonLog.getString("password");
        String userName = cert.getUserName();
        String creditAccount = properties.getCreditPrefix() + userName.toUpperCase() + "_" + cert.getCertAddr().toUpperCase();
        JSONObject jsonArgs = new JSONObject();
        jsonArgs.put("userName", cert.getUserName());
        jsonArgs.put("text", text);
        jsonArgs.put("creditAccount", creditAccount);
        String hexTransaction = TransactionUtils.createHexTrans(cert, Function.DIARYPROOF, jsonArgs, jks_password);
        CustomRepChainClient customRepChainClient = new CustomRepChainClient(properties.getRepchainHost());

        //repchain返回结果
        JSONObject result =null ;
        try {
            result = customRepChainClient.postTransByString(JSON.toJSONString(hexTransaction));
        }catch (IOException e){
            JsonResult.resultJson(response, request,ResponseStatus.REPCHAIN_SERVER_CONNECTION_ERROR,  new ResultData("RepChain服务器连接失败"));
            return;
        } catch (RuntimeException e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.REPCHAIN_SERVER_ERROR, new ResultData("RepChain服务器异常"));
            return;
        }
        System.out.println(result);
        System.out.println("err:"+result.getJSONObject("result").getString("err"));
        if (result.getString("code") == "200" && result.getJSONObject("result").getString("err") == null){
            String txid = result.getString("result"); //获得txid
            // 创建Diary对象
            Diary diary = new Diary();
            diary.setDiaryText(Base64Utils.encode2String(text));
            diary.setUserName(userName);
            diary.setTxId(txid);
            diary.setDiaryId(Base64Utils.encode2String(txid));
            diary.setCreateTime(new Timestamp(System.currentTimeMillis()));

            //创建Integral对象
            Integral integral = new Integral();
            integral.setDiaryId(diary.getDiaryId());
            integral.setScore(1);
            integral.setUserName(userName);
            integral.setTxid(txid);
            integral.setGainTime(new Timestamp(System.currentTimeMillis()));

            //插入数据库
            try {
                userService.pushDairy(diary, integral);
            } catch (Exception e){
                e.printStackTrace();
            }
            JsonResult.resultJson(response, request,ResponseStatus.SUCCESS, result);
        } else {
            JsonResult.resultJson(response, request,ResponseStatus.REPCHAIN_REQUEST_ERROR, result); ;
        }
    }

    /**
     * 从RepChain中查询日志
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "repdiary")
    public void queryDiaryFromRepChain(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject paramBody){
        Cert cert = getCert(request);
        Certificate certificate = null;
        PrivateKey privateKey = null;
        if ("0".equals(cert.getCertStatus())){
            JsonResult.resultJson(response, request, ResponseStatus.DB_CERT_NOT_PROOF, new ResultData("证书未认证"));
            return;
        }
        String diaryKey = properties.getDiaryPrefix() + cert.getUserName().toUpperCase() + "_" + paramBody.getString("day");
        JSONObject jsonArgs = JSONObject.parseObject(diaryKey);
        String hexTransaction = TransactionUtils.createHexTrans(cert, Function.REQURYDIARY, jsonArgs, paramBody.getString("password"));
        CustomRepChainClient customRepChainClient = new CustomRepChainClient(properties.getRepchainHost());
        JSONObject queryResult =null;
        try {
            queryResult = customRepChainClient.postTransByString(hexTransaction);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (queryResult.getString("code") == "200" && queryResult.getJSONObject("result").getString("err") == null){
            JsonResult.resultJson(response, request,ResponseStatus.SUCCESS, queryResult);
        } else {
            JsonResult.resultJson(response, request,ResponseStatus.REPCHAIN_REQUEST_ERROR, queryResult); ;
        }
    }

    /**
     * 查询日志（本地数据库）
     * @param response
     * @param request
     * @return
     */
    @GetMapping(value = "diarylist")
    public void queryDiarySelf(HttpServletResponse response, HttpServletRequest request){
        List<Diary> diaryList = new ArrayList<>();
        String authToken = request.getHeader("Authorization");
        String userName = JWTTokenUtil.parseToken(authToken).getSubject();
        JSONObject jsonObject = new JSONObject();
        try {
            diaryList = userService.queryDiaryList(userName);
            Iterator iterator = diaryList.iterator();
            if (iterator.hasNext()){
                jsonObject.put("diarys", iterator.next());
            }
            JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("success", diaryList));
        } catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("服务器异常"));
        }
    }

    /**
     * 从RepChain中查询积分
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "repcredit")
    public void queryCreditFromRepChain(HttpServletRequest request, HttpServletResponse response, @Param("password") String jks_pass){
        Cert cert = getCert(request);
        if ("0".equals(cert.getCertStatus())){
            JsonResult.resultJson(response, request, ResponseStatus.DB_CERT_NOT_PROOF, new ResultData("证书未认证"));
            return;
        }
        String creditAccount = properties.getCreditPrefix() + cert.getUserName().toUpperCase() + "_" + cert.getCertAddr().toUpperCase();
        JSONObject jsonArgs = JSONObject.parseObject(creditAccount);
        String hexTransaction = TransactionUtils.createHexTrans(cert, Function.QUERYCREDIT, jsonArgs, jks_pass);
        CustomRepChainClient customRepChainClient = new CustomRepChainClient(properties.getRepchainHost());
        JSONObject queryResult =null;
        try {
            queryResult = customRepChainClient.postTransByString(hexTransaction);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (queryResult.getString("code") == "200" && queryResult.getJSONObject("result").getString("err") == null){
            JsonResult.resultJson(response, request,ResponseStatus.SUCCESS, queryResult);
        } else {
            JsonResult.resultJson(response, request,ResponseStatus.REPCHAIN_REQUEST_ERROR, queryResult); ;
        }
    }

    public Cert getCert(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String userName = JWTTokenUtil.parseToken(authHeader).getSubject();
        Cert cert = null;
        try {
            cert = certService.getCertByName(userName);
        }catch (Exception e){
            e.printStackTrace();

        }
        return cert;
    }
}
