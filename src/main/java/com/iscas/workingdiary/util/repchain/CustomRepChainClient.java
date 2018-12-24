package com.iscas.workingdiary.util.repchain;

import com.client.Client;
import com.client.RepChainClient;
import com.crypto.BitcoinUtils;
import com.crypto.ECDSASign;
import com.crypto.Sha256;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.iscas.workingdiary.config.ConstantProperties;
import com.protos.Peer;
import com.utils.certUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.parameters.P;
import sun.security.ec.ECPublicKeyImpl;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 自定义RepChain客户端（原RepChainClient不满足需求：我不希望构建签名交易的jks从文件系统中获取）
 */
public class CustomRepChainClient extends RepChainClient {
    private Certificate certificate;
    private PrivateKey privateKey;

    public CustomRepChainClient() {
        super();
    }

    public CustomRepChainClient(String host, Certificate certificate, PrivateKey privateKey) {
        super(host);
        this.certificate = certificate;
        this.privateKey = privateKey;
    }

    public CustomRepChainClient(String host, String jksPath, String password, String alias, Certificate certificate, PrivateKey privateKey) {
        super(host, jksPath, password, alias);
        this.certificate = certificate;
        this.privateKey = privateKey;
    }

    /**
     * 借用了RCJava中的代码，将原来的从文件系统获取jks更换成传入证书及私钥
     * @param tranType
     * @param chainCodeIdPath
     * @param chaincodeInputFunc
     * @param param
     * @param spcPackage
     * @param chaincodeId
     * @param ctype
     * @return
     * @throws Exception
     */
    public Peer.Transaction createTransWithPK(Peer.Transaction.Type tranType, String chainCodeIdPath,
                                              String chaincodeInputFunc, String param, String spcPackage, String chaincodeId,
                                              Peer.ChaincodeSpec.CodeType ctype) throws Exception{
        if (!ctype.equals(Peer.ChaincodeSpec.CodeType.CODE_JAVASCRIPT)) {
            ctype = Peer.ChaincodeSpec.CodeType.CODE_SCALA;
        } else {
            ctype = Peer.ChaincodeSpec.CodeType.CODE_JAVASCRIPT;
        }
        String name = chaincodeId;
        if (chaincodeId == "") {
            name = Sha256.hashstr(spcPackage);
        } else {
            if (chaincodeId.trim().equals("")) {
                name = Sha256.hashstr(spcPackage);
            } else {
                name = chaincodeId;
            }
        }
        long millis = System.currentTimeMillis() + 8*3600*1000;
        //deploy时取脚本内容hash作为 chaincodeId/name
        //invoke时调用者应该知道要调用的 chaincodeId
        Peer.ChaincodeID cid = Peer.ChaincodeID.newBuilder().setName(name).setPath(chainCodeIdPath).build();
        //构建运行代码
        Peer.ChaincodeInput cip = Peer.ChaincodeInput.newBuilder().setFunction(chaincodeInputFunc).addArgs(param).build();
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
        t.setCert(ByteString.copyFromUtf8(BitcoinUtils.calculateBitcoinAddress(((ECPublicKeyImpl)certificate.getPublicKey()).getEncodedPublicValue())));
        byte[] sig = new ECDSASign().sign(privateKey,Sha256.hash(t.build().toByteArray()));
        t.setSignature(ByteString.copyFrom(sig));
        return t.build();
    }

    /**
     *
     * @param tranType
     * @param chainCodeIdPath
     * @param chaincodeInputFunc
     * @param params
     * @param spcPackage
     * @param chaincodeId
     * @param ctype
     * @return
     * @throws Exception
     */
    public Peer.Transaction createTransWithPK(Peer.Transaction.Type tranType, String chainCodeIdPath,
                                              String chaincodeInputFunc, List params, String spcPackage, String chaincodeId,
                                              Peer.ChaincodeSpec.CodeType ctype) throws Exception{
        if (!ctype.equals(Peer.ChaincodeSpec.CodeType.CODE_JAVASCRIPT)) {
            ctype = Peer.ChaincodeSpec.CodeType.CODE_SCALA;
        } else {
            ctype = Peer.ChaincodeSpec.CodeType.CODE_JAVASCRIPT;
        }
        String name = chaincodeId;
        if (chaincodeId == "") {
            name = Sha256.hashstr(spcPackage);
        } else {
            if (chaincodeId.trim().equals("")) {
                Sha256.hashstr(spcPackage);
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
        t.setCert(ByteString.copyFromUtf8(BitcoinUtils.calculateBitcoinAddress(((ECPublicKeyImpl)certificate.getPublicKey()).getEncodedPublicValue())));
        byte[] sig = new ECDSASign().sign(privateKey,Sha256.hash(t.build().toByteArray()));
        t.setSignature(ByteString.copyFrom(sig));
        return t.build();
    }


    /************************* geter and seter ***********************/
    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
