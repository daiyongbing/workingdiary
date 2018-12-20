package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.bean.Diary;
import com.iscas.workingdiary.bean.Integral;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.service.RepClient;
import com.iscas.workingdiary.service.UserService;
import com.iscas.workingdiary.util.RepChainUtils;
import com.iscas.workingdiary.util.encrypt.Base64Utils;
import com.iscas.workingdiary.bean.ResponseStatus;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import com.iscas.workingdiary.util.json.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/diary")
public class WorkingDairyController {

    @Autowired
    private RepClient repClient;

    @Autowired
    private CertService certService;

    @Autowired
    private UserService userService;

    /**
     * 上传日志
     * @param jsonLog
     * @return
     */
    @PostMapping(value = "pushdairy", produces = "application/json;charset=UTF-8")
    public ResultData postWorkingDairyWithSign(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonLog){
        ResultData resultData = null;
        JSONObject repchainResult;
        // 获得用户名
        String authorization = request.getHeader("Authorization");
        String userName = JWTTokenUtil.parseToken(authorization).getSubject();
        // 从数据库查询证书状态
        Cert cert = null;
        try {
            cert = certService.getCertByName(userName);
        }catch (Exception e){
            e.printStackTrace();
            new Exception("数据库异常");
        }
        // 证书异常，终止上链请求
        if (cert == null || "0".equals(cert.getCertStatus())){
            resultData = new ResultData(ResponseStatus.DB_CERT_NOT_EXIST, "没有证书");
        } else {
            // 正常可用，构建参数提交给区块链，处理异常
            JSONObject jsonObject = new JSONObject();
            String base64Text = Base64Utils.encode2String(jsonLog.toJSONString());
            jsonObject.put("userName", userName);
            jsonObject.put("text", base64Text);
            RepChainClient repChainClient = repClient.getRepClient();
            List<String> argsList = repClient.getParamList(jsonObject);
            String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, repClient.getChaincodeId(),"workingDiaryProof", argsList);
            //repchain返回结果
            repchainResult = repChainClient.postTranByString(hexTransaction);
            String txid = repchainResult.getString("result"); //获得txid

            // 创建Diary对象
            Diary diary = new Diary();
            diary.setDiaryText(base64Text);
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
                resultData = new ResultData(ResponseStatus.SUCCESS, "上传成功");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return resultData;
    }

    /**
     * 查询日志（自己）
     * @param response
     * @param request
     * @return
     */
    @GetMapping(value = "myDiary")
    public List<Diary> queryDiarySelf(HttpServletResponse response, HttpServletRequest request){
        List<Diary> diaryList = new ArrayList<>();
        String authToken = request.getHeader("Authorization");
        String userName = JWTTokenUtil.parseToken(authToken).getSubject();
        try {
            diaryList = userService.queryDiaryList(userName);
        } catch (Exception e){
            e.printStackTrace();
            new Exception("数据库异常");
        }
        return diaryList;
    }


    /**
     * 查询任意用户日志()
     * @param diaryKey
     * @return
     */
    @GetMapping(value = "listdiary")
    public JSONObject queryWorkingDairy(@RequestParam("diaryKey")  String diaryKey){
        RepChainClient repChainClient = repClient.getRepClient();
        List<String> argsList = repClient.getParamList(diaryKey);
        String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, repClient.getChaincodeId(),"queryWorkingDiary", argsList);
        return repChainClient.postTranByString(hexTransaction);
    }

}
