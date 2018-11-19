package com.iscas.workingdiary.util;

import com.alibaba.fastjson.JSON;
import com.client.RepChainClient;
import com.crypto.BitcoinUtils;
import com.crypto.ECDSASign;
import com.crypto.Sha256;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.iscas.workingdiary.bean.TransPram;
import com.protos.Peer;
import sun.security.ec.ECPublicKeyImpl;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.utils.certUtil.getCertFromJKS;

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
