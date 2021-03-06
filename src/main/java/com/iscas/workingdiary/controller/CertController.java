package com.iscas.workingdiary.controller;

import com.alibaba.fastjson.JSONObject;
import com.crypto.BitcoinUtils;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.config.ConstantProperties;
import com.iscas.workingdiary.service.CertService;
import com.iscas.workingdiary.util.FileUtils;
import com.iscas.workingdiary.util.cert.CertificateUtils;
import com.iscas.workingdiary.util.encrypt.Base64Utils;
import com.iscas.workingdiary.util.encrypt.MD5Utils;
import com.iscas.workingdiary.bean.ResponseStatus;
import com.iscas.workingdiary.util.jjwt.JWTTokenUtil;
import com.iscas.workingdiary.util.json.JsonResult;
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
    ConstantProperties properties;

    /**
     * 删除证书（根据证书编号）
     * @param certNo
     * @return
     */
    @GetMapping(value = "deleteByCertNo", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteCertByCertNo(HttpServletResponse response, HttpServletRequest request, @RequestParam("certNO") String certNo){
        try {
            certService.deleteCertByCertNo(certNo);
            JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("证书已删除"));
        }catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("服务器异常"));
        }
    }

    @GetMapping(value = "deleteByUserId", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteCertByCertNo(HttpServletRequest request, HttpServletResponse response, @RequestParam("userId") Integer userId){
        try {
            //certService.deleteCertByUserId(userId);
            JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("证书已删除"));
        }catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("服务器异常"));
        }
    }

    /**
     * 查询证书（根据证书编号）
     * @param certNo
     * @return
     */
    @GetMapping(value = "queryByCertNo", produces = MediaType.APPLICATION_JSON_VALUE)
    public void queryCertByNo(HttpServletResponse response, HttpServletRequest request, @RequestParam("certNo") String certNo){
        Cert cert = null;
        try {
            cert =  certService.queryCert(certNo);
            JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("查询成功", cert));
        } catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("服务器异常"));
        }
    }

    /**
     * 查询所有证书
     * @return
     */
    @GetMapping(value = "queryAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Cert> queryAllCert(){
        List<Cert> certList;
        certList = certService.selectAll();
        return certList;
    }

    /**
     * 更新证书
     * @param cert
     */
    @PostMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateCert(Cert cert){
        certService.updateCert(cert);
    }


    /**
     * 验证证书
     * @param userId
     * @return
     */
   /* @GetMapping(value = "verifyCert", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object verifyCert(@RequestParam("userId") Integer userId){
        ResultData resultData = null;
        Cert cert = certService.verifyCert(userId);
        if (cert != null){
            switch (cert.getCertStatus()){
                // 证书是否已上链，考虑到数据库的状态能被人为修改，最好是直接向RepChain请求验证
                case "0":
                    resultData = new ResultData(StateCode.DB_CERT_NOT_PROOF, "未认证的用户证书");
                    break;
                case "1":
                    resultData = new ResultData(StateCode.SUCCESS, "证书验证成功");
                    break;
                default:
                    resultData = new ResultData(StateCode.DB_SUCCESS_BUT_UNEXPECTED_RESULT, "unexpected result");
            }
        } else {
            resultData = new ResultData(StateCode.DB_CERT_NOT_EXIST,"没有证书");
        }

        return resultData;
    }*/


    /**
     * 在线生成证书
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public void generateCert(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject){
        String[] certInfo= {jsonObject.getString("CN"), jsonObject.getString("OU"), jsonObject.getString("O"),
                jsonObject.getString("C"), jsonObject.getString("L"), jsonObject.getString("ST")};
        String password = jsonObject.getString("password");  // 密码用于私钥加密和解密，不做保存，一旦忘记不可找回
        CertificateUtils certUtils = new CertificateUtils();
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
            pemCert = Base64Utils.encode2String(certUtils.getPemFromCertificate(certificate));     // 获取pemcert
            certNo = MD5Utils.stringMD5(pemCert); //使用pemCert的MD5作为证书编号
            encyptPrivateKey = certUtils.encryptPrivateKey(keyPair.getPrivate(), password);    //加密私钥
        }catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("证书生成失败"));
            return;
        }

        Cert cert = new Cert();
        cert.setCertNo(certNo);
        cert.setCertAddr(addr);
        cert.setPemCert(pemCert);
        cert.setCertLevel("0");
        cert.setCertStatus("0");
        cert.setPrivateKey(encyptPrivateKey);

        try {
                certService.insertCert(cert);
                certUtils.generateJksWithCert(certificate, keyPair, password, properties.getJksPath(), certInfo[0]);   //保存jks文件到服务器
                certUtils.savePemCertAsFile(certificate, properties.getCertPath(), certInfo[0]); // 保存cer到服务器
                JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("success", addr));
        }catch (Exception e){
            e.printStackTrace();
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("插入数据库失败"));
        }
    }

    /**
     * 上传证书
     * @param file
     * @return
     */
    @PostMapping(value = "upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadCert(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file){
        String authHeader = request.getHeader("");
        ResultData resultData;
        String fileName = file.getOriginalFilename();
        String md5 = "";
        if(file.isEmpty() || file.getSize()>1048576){ // 文件最大2M
            JsonResult.resultJson(response, request, ResponseStatus.SERVER_PARAM_ERROR, new ResultData("文件最大为2M且不能为空"));
            return;
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
                JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("success"));
            } catch (Exception e) {
                e.printStackTrace();
                JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR, new ResultData("服务器异常"));
            }
        }
    }

    /**
     * 下载证书
     * @param response
     * @param request
     */
    @GetMapping(value = "download", produces = MediaType.APPLICATION_JSON_VALUE)
    public void downloadCert(HttpServletResponse response, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        String userName = JWTTokenUtil.parseToken(token).getSubject();
        String pemCert = null;
        try {
            pemCert = certService.getPemCert(userName);  // 从数据库中获取证书
        }catch (Exception e){
            e.printStackTrace();
        }

        if (pemCert == null){
            try {
                response.setCharacterEncoding("UTF-8");
                response.setStatus(307);
                response.getWriter().write("该用户没有证书");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String certString = Base64Utils.decode2String(pemCert);
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + userName+".cer");// 设置文件名
            try(OutputStream os = response.getOutputStream()) {
                os.write(certString.getBytes());
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
