package com.iscas.workingdiary.util.repchain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.config.ConstantProperties;
import com.iscas.workingdiary.util.Byte2Hex;
import com.iscas.workingdiary.util.cert.CertificateUtils;
import com.iscas.workingdiary.util.encrypt.Base64Utils;
import com.protos.Peer;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建RepChain签名交易（仅仅针对特定合约，非通用）
 */
public class TransactionUtils {

    @Autowired
    private static ConstantProperties properties;

    /**
     *  {
     *     "userName":"String",
     *     "pemCert":"String",
     *     "certInfo":"String"
     *  }
     * @param adminCert
     * @param jsonObject
     * @param adminPassword
     * @return
     */
    public static String register(Cert adminCert, JSONObject jsonObject, String adminPassword){
        PrivateKey privateKey = null;
        String hexTransaction = null;
        //反序列化adminCert
        String adminPemCert = Base64Utils.decode2String(adminCert.getPemCert());
        Certificate certificate = CertificateUtils.getCertByPem(adminPemCert);
        String cryptedprivateKey = adminCert.getPrivateKey();
        try {
            privateKey = CertificateUtils.decryptPrivateKey(cryptedprivateKey, adminPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CustomRepChainClient customRepChainClient = new CustomRepChainClient(certificate, privateKey);
        List<String> argsList = new ArrayList<>();
        argsList.add(JSON.toJSONString(jsonObject));
        Peer.Transaction transaction;
        try {
            transaction = customRepChainClient.createTransWithPK(Peer.Transaction.Type.CHAINCODE_INVOKE, "path",
                    "register", argsList, "string", properties.getChaincodeId(), Peer.ChaincodeSpec.CodeType.CODE_SCALA);
            System.out.println(transaction);
            hexTransaction = Byte2Hex.bytes2hex(transaction.toByteArray());
        }catch (Exception e){
            e.printStackTrace();
        }
        return hexTransaction;
    }

    /**
     * 创建16进制签名交易（使用用户证书）
     * @param cert
     * @param funcName
     * @param jsonArgs
     * @param pk_password
     * @return
     */
    public static String createHexTrans(Cert cert, String funcName, JSONObject jsonArgs, String pk_password){
        PrivateKey privateKey = null;
        String hexTransaction = null;
        //反序列化adminCert
        String pemCert = Base64Utils.decode2String(cert.getPemCert());
        Certificate certificate = CertificateUtils.getCertByPem(pemCert);
        try {
            //解密私钥
            privateKey = CertificateUtils.decryptPrivateKey(cert.getPrivateKey(), pk_password);
        } catch (Exception e) {
            e.printStackTrace();
            new Exception("私钥解析失败");
        }

        CustomRepChainClient customRepChainClient = new CustomRepChainClient(certificate, privateKey);
        List<String> argsList = new ArrayList<>();
        argsList.add(JSON.toJSONString(jsonArgs));
        Peer.Transaction transaction;
        try {
            transaction = customRepChainClient.createTransWithPK(Peer.Transaction.Type.CHAINCODE_INVOKE, "path",
                    funcName, argsList, "string", properties.getChaincodeId(), Peer.ChaincodeSpec.CodeType.CODE_SCALA);
            System.out.println(transaction);
            hexTransaction = Byte2Hex.bytes2hex(transaction.toByteArray());
        }catch (Exception e){
            e.printStackTrace();
        }
        return hexTransaction;
    }
}
