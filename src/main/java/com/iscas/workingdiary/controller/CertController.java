package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.service.RepClient;
import com.iscas.workingdiary.util.FileUtils;
import com.iscas.workingdiary.util.RepChainUtils;
import com.iscas.workingdiary.util.json.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 证书管理类
 */

@RestController
@RequestMapping("/cert")
public class CertController {

    @Autowired
    private CertService certService;

    @Autowired
    private RepClient repClient;

    @PostMapping("insert")
    public void insertCert(Cert cert){
        FileInputStream jks = FileUtils.getFis("C:/Users/vic/Desktop/wd_jks/"+cert.getCommonName()+".jks");
        FileInputStream pemCert = FileUtils.getFis("C:/Users/vic/Desktop/wd_jks/"+cert.getCommonName()+".cer");
        //cert.setJks(jks);
        //cert.setPemCert(pemCert);
        certService.insertCert(cert);
    }

    @GetMapping("deleteByCertNo")
    public void deleteCertByCertNo(String certNo){
        certService.deleteCertByCertNo(certNo);
    }

    @GetMapping("deleteByUserId")
    public void deleteCertByCertNo(Integer userId){
        certService.deleteCertByUserId(userId);
    }

    @GetMapping("queryByCertNo")
    public Cert queryCertByNo(String certNo){
        Cert cert = new Cert();
        cert =  certService.queryCert(certNo);
        return cert;
    }

    @GetMapping("queryAll")
    public List<Cert> queryAllCert(){
        List<Cert> certList = new ArrayList<>();
        certList = certService.selectAll();
        return certList;
    }

    @PostMapping("update")
    public void updateCert(Cert cert){
        certService.updateCert(cert);
    }


    @GetMapping(value = "verifyCert", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object verifyCert(@RequestParam("userId") Integer userId){
        ResultData resultData = null;
        Cert cert = certService.verifyCert(userId);
        if (cert != null){
            switch (cert.getCertStatus()){
                // 证书是否已上链，考虑到数据库的状态能被人为修改，最好是直接向RepChain请求验证
                case 0:
                    resultData = new ResultData(ResultData.CERT_NOT_PROOF, "未认证的用户证书");
                    break;
                case 1:
                    resultData = new ResultData(ResultData.CODE_SUCCESS, "证书验证成功");
                    break;
                default:
                    resultData = new ResultData(ResultData.CODE_ERROR_OTHER, "未知错误");
            }
        } else {
            resultData = new ResultData(ResultData.NO_CERT,"没有证书");
        }

        return resultData;
    }


    /********************************************以下接口针对区块链****************************************/

    @PostMapping("signCert")
    public JSONObject signCert(@RequestBody JSONObject param){
        RepChainClient repChainClient = repClient.getRepClient();
        List<String> argsList = repClient.getParamList(param);
        String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, repClient.getChaincodeId(),"certProof", argsList);
        return repChainClient.postTranByString(hexTransaction);
    }

    @GetMapping(value = "destroy")
    public JSONObject destroyCert(@RequestParam("certAddr") String addr){
        RepChainClient repChainClient = repClient.getRepClient();
        List<String> argsList = repClient.getParamList(addr);
        String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, repClient.getChaincodeId(),"destroyCert", argsList);
        return repChainClient.postTranByString(hexTransaction);
    }

    @PostMapping("replace")
    public JSONObject replaceCert(@RequestBody JSONObject data){
        RepChainClient repChainClient = repClient.getRepClient();
        List<String> argsList = repClient.getParamList(data);
        String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, repClient.getChaincodeId(),"replaceCert", argsList);
        return repChainClient.postTranByString(hexTransaction);
    }
}
