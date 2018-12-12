package com.iscas.workingdiary.util;

import com.alibaba.fastjson.JSON;
import com.client.RepChainClient;
import com.protos.Peer;
import java.util.List;

/**
 * RepChain工具类
 */
public class RepChainUtils {

    public static String createHexTransaction(RepChainClient repChainClient, String chainCodeId, String functionName, List args ){

        Peer.Transaction transaction = null;
        try {
            transaction = repChainClient.createTransaction(Peer.Transaction.Type.CHAINCODE_INVOKE, "path",
                    functionName, args, "string", chainCodeId, Peer.ChaincodeSpec.CodeType.CODE_SCALA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(transaction);
        String hexTransaction = Byte2Hex.bytes2hex(transaction.toByteArray());
        return JSON.toJSONString(hexTransaction);
    }
}
