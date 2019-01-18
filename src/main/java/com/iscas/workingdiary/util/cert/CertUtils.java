package com.iscas.workingdiary.util.cert;

import com.alibaba.fastjson.JSON;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.bean.CertInfo;
import com.iscas.workingdiary.util.encrypt.AESCrypt;
import com.iscas.workingdiary.util.encrypt.Base64Utils;
import com.iscas.workingdiary.util.encrypt.MD5Utils;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;

/**
 * 针对Cert实体的工具类
 */
public class CertUtils {

    public static Cert genCert(CertInfo certinfo, String jks_path, String certPath){
        //password 密码用于私钥加密和解密，不做保存，一旦忘记不可找回
        String[] certInfo= {certinfo.getCN(), certinfo.getOU(), certinfo.getO(),
                certinfo.getC(), certinfo.getL(), certinfo.getST()};
        String password = certinfo.getcPassword();

        CertificateUtils certUtils = new CertificateUtils();
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
            CertificateUtils.encryptPrivateKey(keyPair.getPrivate(), password); //加密私钥
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
            certUtils.generateJksWithCert(certificate, keyPair, password, jks_path, certInfo[0]);   //保存jks文件到服务器
            certUtils.savePemCertAsFile(certificate, certPath, certInfo[0]); // 保存cer到服务器
        }catch (Exception e){
            e.printStackTrace();
        }
        return cert;
    }
}
