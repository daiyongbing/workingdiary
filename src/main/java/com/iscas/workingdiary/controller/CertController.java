package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.service.RepClient;
import com.iscas.workingdiary.util.FileUtils;
import com.iscas.workingdiary.util.RepChainUtils;
import com.iscas.workingdiary.util.encrypt.MD5Utils;
import com.iscas.workingdiary.util.json.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    /**
     * 插入mysql 直接存文件还是存地址？
     * @param cert
     */
    @PostMapping(value = "insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertCert(Cert cert){
        FileInputStream jks = FileUtils.getFis("C:/Users/vic/Desktop/wd_jks/"+cert.getCommonName()+".jks");
        FileInputStream pemCert = FileUtils.getFis("C:/Users/vic/Desktop/wd_jks/"+cert.getCommonName()+".cer");
        //cert.setJks(jks);
        //cert.setPemCert(pemCert);
        certService.insertCert(cert);
    }

    @GetMapping(value = "deleteByCertNo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData deleteCertByCertNo(@RequestParam("certNO") String certNo){
        ResultData resultData = null;
        try {
            certService.deleteCertByCertNo(certNo);
            resultData = ResultData.deleteSuccess();
        }catch (Exception e){
            resultData = new ResultData(ResultData.DATABASE_EXCEPTION, "删除失败");
        }
        return resultData;
    }

    @GetMapping(value = "deleteByUserId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData deleteCertByCertNo(@RequestParam("userId") Integer userId){
        ResultData resultData = null;
        try {
            certService.deleteCertByUserId(userId);
            resultData = ResultData.deleteSuccess();
        }catch (Exception e){
            resultData = new ResultData(ResultData.DATABASE_EXCEPTION, "删除失败");
        }
        return resultData;
    }

    @GetMapping(value = "queryByCertNo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData queryCertByNo(@RequestParam("certNo") String certNo){
        Cert cert = null;
        ResultData resultData = null;
        try {
            cert =  certService.queryCert(certNo);
            resultData = new ResultData(ResultData.CODE_SUCCESS, "查询成功", cert);
        } catch (Exception e){
            resultData = new ResultData(ResultData.DATABASE_EXCEPTION, "数据库异常");
        }

        return resultData;
    }

    @GetMapping(value = "queryAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Cert> queryAllCert(){
        List<Cert> certList = new ArrayList<>();
        certList = certService.selectAll();
        return certList;
    }

    @PostMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "upload")
    public ResultData uploadCert(@RequestParam("fileName") MultipartFile file){
        ResultData resultData = null;
        String fileName = file.getOriginalFilename();
        String md5 = "";
        if(file.isEmpty() || file.getSize()>1048576){ // 文件最大2M
            resultData = new ResultData(ResultData.CODE_ERROR_PARAM, "文件最大为2M且不能为空");
        } else {
            try {
                md5 = MD5Utils.bytesMD5(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String storeName = md5+FileUtils.getFileExt(fileName);
            long size = file.getSize();
            System.out.println(storeName + "-->" + size);

            String path = "G:/workingdiary/uploadCert" ;
            File dest = new File(path + "/" + storeName);
            if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }
            try {
                file.transferTo(dest); //保存文件
                resultData = new ResultData(ResultData.CODE_SUCCESS, "success");
            } catch (IllegalStateException e) {
                resultData = new ResultData(ResultData.CODE_ERROR_EXCEPTION, "IllegalStateException");
            } catch (IOException e) {
                resultData = new ResultData(ResultData.CODE_ERROR_EXCEPTION, "IOException");
            }
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
