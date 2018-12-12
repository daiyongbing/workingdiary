package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.client.RepChainClient;
import com.crypto.BitcoinUtils;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.config.ConstantProperties;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.service.RepClient;
import com.iscas.workingdiary.util.FileUtils;
import com.iscas.workingdiary.util.RepChainUtils;
import com.iscas.workingdiary.util.cert.CertUtils;
import com.iscas.workingdiary.util.encrypt.Base64Utils;
import com.iscas.workingdiary.util.encrypt.MD5Utils;
import com.iscas.workingdiary.util.exception.StateCode;
import com.iscas.workingdiary.util.json.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
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

    @Autowired
    ConstantProperties properties;

    /**
     * 插入mysql 直接存文件还是存地址？
     * @param cert
     */
    @PostMapping(value = "insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public void insertCert(Cert cert){
       /* FileInputStream jks = FileUtils.getFis("C:/Users/vic/Desktop/wd_jks/"+cert.getCommonName()+".jks");
        FileInputStream pemCert = FileUtils.getFis("C:/Users/vic/Desktop/wd_jks/"+cert.getCommonName()+".cer");*/
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
            resultData = new ResultData(StateCode.DB_DELETE_ERROR, "删除失败");
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
            resultData = new ResultData(StateCode.DB_DELETE_ERROR, "删除失败");
        }
        return resultData;
    }

    @GetMapping(value = "queryByCertNo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData queryCertByNo(@RequestParam("certNo") String certNo){
        Cert cert = null;
        ResultData resultData = null;
        try {
            cert =  certService.queryCert(certNo);
            resultData = new ResultData(StateCode.SUCCESS, "查询成功", cert);
        } catch (Exception e){
            resultData = new ResultData(StateCode.DB_QUERY_ERROR, "查询失败");
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
                    resultData = new ResultData(StateCode.DB_CERT_NOT_PROOF, "未认证的用户证书");
                    break;
                case 1:
                    resultData = new ResultData(StateCode.SUCCESS, "证书验证成功");
                    break;
                default:
                    resultData = new ResultData(StateCode.DB_SUCCESS_BUT_UNEXPECTED_RESULT, "unexpected result");
            }
        } else {
            resultData = new ResultData(StateCode.DB_CERT_NOT_EXIST,"没有证书");
        }

        return resultData;
    }


    @PostMapping(value = "generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData generateCert(@RequestBody JSONObject jsonObject){
        ResultData resultData;
        String[] certInfo= {jsonObject.getString("CN"), jsonObject.getString("OU"), jsonObject.getString("O"),
                jsonObject.getString("C"), jsonObject.getString("L"), jsonObject.getString("ST")};
        String password = jsonObject.getString("password");  // 密码用于私钥加密和解密，不做保存，一旦忘记不可找回
        CertUtils certUtils = new CertUtils();
        KeyPair keyPair = null;
        X509Certificate certificate = null;
        String addr = "";
        String pemCert = "";
        String certNo = "";
        String encyptPrivateKey = "";
        try {
            keyPair = certUtils.generateKeyPair();
            certificate = certUtils.generateCert(certInfo, keyPair);    // generate cert
            addr = BitcoinUtils.calculateBitcoinAddress(certificate.getPublicKey().getEncoded());    //计算证书短地址
            pemCert = certUtils.getCertPEM(certificate);     // 获取pemcert
            certNo = MD5Utils.stringMD5(pemCert); //使用pemCert的MD5作为证书编号
            encyptPrivateKey = certUtils.encryptPrivateKey(keyPair.getPrivate(), password);    //加密私钥
        }catch (Exception e){
            e.getMessage();
        }

        Cert cert = new Cert();
        cert.setCertNo(certNo);
        cert.setCertAddr(addr);
        cert.setPemCert(pemCert);
        cert.setCertLevel("0");
        cert.setCertStatus("0");

        try {
            Cert existCert = certService.verifyCert(cert.getUserId());
            if (existCert != null){
                resultData = new ResultData(StateCode.DB_ALREADY_EXIST_ERROR, "证书已存在", existCert.getCertAddr());
            }else {
                certService.insertCert(cert);
                certUtils.generateJksWithCert(certificate, keyPair, password, properties.getJksPath(), certInfo[0]);   //保存jks文件到服务器
                certUtils.saveCertAsPEM(certificate, properties.getCertPath(), certInfo[0]); // 保存cer到服务器
                resultData = new ResultData(StateCode.SUCCESS, "success", addr);
            }
        }catch (Exception e){
            resultData = new ResultData(StateCode.DB_INSERT_ERROR, "证书插入失败");
        }
        return resultData;
    }

    @PostMapping(value = "upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData uploadCert(@RequestParam("fileName") MultipartFile file){
        ResultData resultData = null;
        String fileName = file.getOriginalFilename();
        String md5 = "";
        if(file.isEmpty() || file.getSize()>1048576){ // 文件最大2M
            resultData = new ResultData(StateCode.SERVER_PARAM_ERROR, "文件最大为2M且不能为空");
        } else {
            try {
                md5 = MD5Utils.bytesMD5(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String storeName = md5+FileUtils.getFileExt(fileName);
            long size = file.getSize();
            System.out.println(storeName + "-->" + size);
            String path = properties.getCertPath();
            File dest = new File(path + "/" + storeName);
            System.out.println(dest.getAbsolutePath());
            if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest); //保存文件
                resultData = new ResultData(StateCode.SUCCESS, "success");
            } catch (IllegalStateException e) {
                resultData = new ResultData(StateCode.SERVER_ERROR, "IllegalStateException");
            } catch (IOException e) {
                resultData = new ResultData(StateCode.SERVER_ERROR, "IOException");
            }
        }
        return resultData;
    }

    @GetMapping(value = "download", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultData downloadCert(HttpServletResponse response, HttpServletRequest request, @RequestParam("userId") Integer userId){
        // 暂时使用参数，以后从session中获取
        ResultData resultData = null;
        X509Certificate certificate;
        Cert certBean = null;
        try {
            certBean = certService.verifyCert(userId);  // 从数据库中获取证书
        }catch (Exception e){
            e.printStackTrace();
        }

        if (certBean == null){
            resultData = new ResultData(StateCode.DB_CERT_NOT_EXIST, "没有证书");
        } else {
            String pemCert = certBean.getPemCert();
            String certString = Base64Utils.decode2String(pemCert);
            //response.setContentType("application/force-download");// 设置强制下载不打开
            response.setContentType("application/json");
            //response.addHeader("Content-Disposition", "attachment;fileName=" + certBean.getCommonName()+".cer");// 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + certBean.getCommonName()+".cer");// 设置文件名
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                os.write(certString.getBytes());
                os.flush();
                resultData = new ResultData(StateCode.SUCCESS, "下载成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (os != null){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return resultData;
    }


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
