package com.iscas.workingdiary.util.repchain;

import com.alibaba.fastjson.JSON;
import com.client.RepChainClient;
import com.iscas.workingdiary.config.ConstantProperties;
import com.iscas.workingdiary.util.Byte2Hex;
import com.protos.Peer;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * RepChain工具类
 */
public class RepChainUtils {

    @Autowired
    private static ConstantProperties properties;

    private static String chaincodeId = properties.getChaincodeId();

    public static List getParamList(Object o) {
        String jsonStr = JSON.toJSONString(o);
        List<String> argsList = new ArrayList<>();
        argsList.add(jsonStr);
        return argsList;
    }



    public static String createHexTransWithListParam(CustomRepChainClient customRepChainClient, String functionName, List args ){

        Peer.Transaction transaction = null;
        try {
            transaction = customRepChainClient.createTransWithPK(Peer.Transaction.Type.CHAINCODE_INVOKE, "path",
                    functionName, args, "string", chaincodeId, Peer.ChaincodeSpec.CodeType.CODE_SCALA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(transaction);
        String hexTransaction = Byte2Hex.bytes2hex(transaction.toByteArray());
        return JSON.toJSONString(hexTransaction);
    }

    public static String createHexTransWithStrParam(CustomRepChainClient customRepChainClient, String functionName, String args ){

        Peer.Transaction transaction = null;
        try {
            transaction = customRepChainClient.createTransWithPK(Peer.Transaction.Type.CHAINCODE_INVOKE, "path",
                    functionName, args, "string", chaincodeId, Peer.ChaincodeSpec.CodeType.CODE_SCALA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hexTransaction = Byte2Hex.bytes2hex(transaction.toByteArray());
        return JSON.toJSONString(hexTransaction);
    }
}
