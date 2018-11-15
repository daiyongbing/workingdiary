package com.iscas.workingdiary.util;

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

    /**
     * 创建交易的公共方法
     *
     * @param tranType
     * @param chainCodeIdPath
     * @param chaincodeInputFunc
     * @param params
     * @param spcPackage
     * @param chaincodeId
     * @return
     */
    protected static Peer.Transaction transactionCreator(Peer.Transaction.Type tranType, String chainCodeIdPath,
                                                         String chaincodeInputFunc, List params, String spcPackage, String chaincodeId,
                                                         Peer.ChaincodeSpec.CodeType ctype) throws Exception{
        if (!ctype.equals(Peer.ChaincodeSpec.CodeType.CODE_JAVASCRIPT)) {
            ctype = Peer.ChaincodeSpec.CodeType.CODE_SCALA;
        } else {
            ctype = Peer.ChaincodeSpec.CodeType.CODE_JAVASCRIPT;
        }
        String name = chaincodeId;
        if (chaincodeId == "") {
            name = new Sha256().hashstr(spcPackage);
        } else {
            if (chaincodeId.trim().equals("")) {
                new Sha256().hashstr(spcPackage);
            } else {
                name = chaincodeId;
            }
        }
        long millis = System.currentTimeMillis() + 8*3600*1000;
        //deploy时取脚本内容hash作为 chaincodeId/name
        //invoke时调用者应该知道要调用的 chaincodeId
        Peer.ChaincodeID cid = Peer.ChaincodeID.newBuilder().setName(name).setPath(chainCodeIdPath).build();
        //构建运行代码
        Peer.ChaincodeInput cip = Peer.ChaincodeInput.newBuilder().setFunction(chaincodeInputFunc).addAllArgs(params).build();
        //初始化链码
        Peer.ChaincodeSpec chaincodeSpec = Peer.ChaincodeSpec.newBuilder().setChaincodeID(cid).setCtorMsg(cip).setTimeout(1000).
                setSecureContext("secureContext").setCodePackage(ByteString.copyFromUtf8(spcPackage)).setCtype(ctype).build();

        Peer.Transaction.Builder t = Peer.Transaction.newBuilder();
        t.setType(tranType).setChaincodeID(ByteString.copyFromUtf8(cid.toString()))
                .setPayload(chaincodeSpec).setMetadata(ByteString.copyFromUtf8("")).setTxid("")
                .setTimestamp(Timestamp.newBuilder().setSeconds(millis/1000).setNanos((int)(millis % 1000) * 1000000))
                .setConfidentialityLevel(Peer.ConfidentialityLevel.PUBLIC).setConfidentialityProtocolVersion( "confidentialityProtocolVersion-1.0")
                .setNonce(ByteString.copyFromUtf8("nonce")).setToValidators(ByteString.copyFromUtf8("toValidators"))
                .setCert(ByteString.EMPTY).setSignature(ByteString.EMPTY);
        String  txid = "";
        if (tranType.equals(Peer.Transaction.Type.CHAINCODE_DEPLOY)){
            txid = name;
        } else {
            txid = UUID.randomUUID().toString();
        }
        t = t.setTxid(txid);
        //List list = getCertFromJKS(new File("jks/daiyongbing.jks"),"123","daiyongbing");
        List list = getCertFromJKS(new File("jks/mykeystore_1.jks"),"123","1");
        //List list = getCertFromJKS(new File("jks/mykeystore_2.jks"),"123","2");
        //List list = getCertFromJKS(new File("jks/keystore_admin.jks"),"super_admin","super_admin");
        Certificate cert = (Certificate) list.get(0);
        //t.setCert(ByteString.copyFromUtf8(BitcoinUtils.calculateBitcoinAddress(cert.getPublicKey().getEncoded()))); // 旧版本
        t.setCert(ByteString.copyFromUtf8(BitcoinUtils.calculateBitcoinAddress(((ECPublicKeyImpl)cert.getPublicKey()).getEncodedPublicValue()))); //新版本
        byte[] sig = new ECDSASign().sign((PrivateKey) list.get(1),Sha256.hash(t.build().toByteArray()));
        System.out.println("no Sig:"+t.build());
        t.setSignature(ByteString.copyFrom(sig));
        return t.build();
    }

    public static String createHexTransaction(String chainCodeId, String functionName, List args ){

        Peer.Transaction transaction = null;
        try {
            transaction = RepChainUtils.transactionCreator(Peer.Transaction.Type.CHAINCODE_INVOKE, "path",
                    functionName, args, "string", chainCodeId, Peer.ChaincodeSpec.CodeType.CODE_SCALA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(transaction);
        String HexTransaction = Byte2Hex.bytes2hex(transaction.toByteArray());
        return HexTransaction;
    }
}
