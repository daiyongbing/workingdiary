package com.iscas.workingdiary.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

public class CertUtils {
    /**
     * 通过配置信息获取证书（动态加载）
     *
     * @param jks_file
     * @param password
     * @param alias
     * @return
     */
    public static List getCertFromInputStram(FileInputStream fis, String password, String alias) {
        try {
            KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] pwd = password.toCharArray();
            store.load(fis, pwd);
            Key sk = store.getKey(alias, pwd);
            Certificate cert = store.getCertificate(alias);
            PrivateKey privateKey = (PrivateKey)sk;
            List list = new ArrayList();
            list.add(cert);
            list.add(privateKey);
            list.add(cert.getPublicKey());
            return list;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
