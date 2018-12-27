package com.iscas.workingdiary.util.cert;

import java.security.*;
import java.security.cert.X509Certificate;

public class CertUtilsTest {

    public static void main(String[] args){
        String path = "G:/workingdiary/cert";
        String[] info = { "daiyongbing", "ISCAS", "ISCAS", "CN", "GUIZHOU", "GUIYANG" };
        String jks_password = "123";
        CertificateUtils certUtils = new CertificateUtils();
        KeyPair keyPair = certUtils.generateKeyPair();  //生成KeyPair
        X509Certificate certificate = certUtils.generateCert(info,keyPair);  //生成证书
        System.out.println(certificate.toString());

        certUtils.generateJksWithCert(certificate, keyPair, jks_password, path, "daiyongbing");
    }
}
