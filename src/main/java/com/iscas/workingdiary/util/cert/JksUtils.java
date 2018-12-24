package com.iscas.workingdiary.util.cert;

import com.iscas.workingdiary.util.encrypt.Base64Utils;
import com.iscas.workingdiary.util.encrypt.MD5Utils;
import com.utils.certUtil;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;

public class JksUtils {

    /**
     * 只做测试，不做其他用途
     * @param jksPath
     * @param password
     * @param alias
     * @return
     */
    public static List loadCertFromJKS(String jksPath, String password , String alias){
        return certUtil.getCertFromJKS(new File(jksPath),password,alias);
    }

    public static void main(String[] args){
        List list = loadCertFromJKS("C:/Users/vic/Desktop/from.jks", "123", "from");
        CertUtils certUtils = new CertUtils();
        String pemCert = Base64Utils.encode2String(certUtils.getPemFromCertificate((Certificate) list.get(0)));     // 获取pemcert
        String encyptPrivateKey = certUtils.encryptPrivateKey((PrivateKey)list.get(1), "123456");    //加密私钥

        System.out.println("pemCert:"+pemCert+"\nencyptPrivateKey:"+encyptPrivateKey);
    }
}
