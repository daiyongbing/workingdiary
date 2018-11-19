package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.service.RepClient;
import com.iscas.workingdiary.util.RepChainUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /********************************************以下接口针对区块链****************************************/

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

    @PostMapping("sign")
    public JSONObject signCert(@RequestParam("cert") String pemCert){
        RepChainClient repChainClient = repClient.getRepClient();
        List<String> argsList = repClient.getParamList(pemCert);
        String hexTransaction = RepChainUtils.createHexTransaction(repChainClient, repClient.getChaincodeId(),"signCert", argsList);
        return repChainClient.postTranByString(hexTransaction);
    }
}
