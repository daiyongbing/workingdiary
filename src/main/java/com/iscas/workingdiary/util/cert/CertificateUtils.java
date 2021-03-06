package com.iscas.workingdiary.util.cert;

import com.iscas.workingdiary.util.encrypt.AESCrypt;
import com.iscas.workingdiary.util.encrypt.Base64Utils;
import com.iscas.workingdiary.util.encrypt.MD5Utils;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Date;
import java.util.Random;

/**
 * 证书工具类
 * @author daiyongbing
 */
public class CertificateUtils {
    private final static String ALGORITHM_CURVE_SECP256K1 = "secp256k1";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 生成KeyPair
     * @return keyPair
     */
    public KeyPair generateKeyPair(){
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(ALGORITHM_CURVE_SECP256K1);
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    /**
     * 生成X509证书
     * @param certInfo String[8] = {CN:"Common Name", OU:"Organisation Unit", O:"Organisation Name", C:"Country", L:"Locality Name", ST:""State Name}
     * @param keyPair 这里使用自签名
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchProviderException
     * @throws SecurityException
     * @throws SignatureException
     */
    public X509Certificate generateCert(String[] certInfo, KeyPair keyPair) {
        X509V3CertificateGenerator cerGenerator = new X509V3CertificateGenerator();
        X509Certificate cert = null;
        cerGenerator.setSerialNumber(new BigInteger(256, new Random()));  // 序列号
        cerGenerator.setIssuerDN(new X509Name("CN="+certInfo[0]+", OU="+certInfo[1]+", O="+certInfo[2]+" , C="+certInfo[3]));
        cerGenerator.setNotBefore(new Date(System.currentTimeMillis()));
        cerGenerator.setNotAfter(new Date((System.currentTimeMillis()+365*24*60*60*1000L)));
        cerGenerator.setSubjectDN(new X509Name("CN=" + certInfo[0] + ",OU=" + certInfo[1] + ",O=" + certInfo[2] + ",C=" + certInfo[3] + ",L="
                + certInfo[4] + ",ST=" + certInfo[5]));
        cerGenerator.setPublicKey(keyPair.getPublic());
        //certificateGenerator.setSignatureAlgorithm("SHA1WithRSA");
        cerGenerator.setSignatureAlgorithm("SHA256WithECDSA");
        try {
            cert = cerGenerator.generateX509Certificate(keyPair.getPrivate(), "BC");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return cert;
    }

    /**
     * 将Certificate转换成pem字符串
     * @param certificate
     * @return
     */
    public String getPemFromCertificate(Certificate certificate){
        String encode = null;
        try {
            encode = Base64Utils.encode2String(certificate.getEncoded());
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        StringBuffer stringBuffer =  new StringBuffer(encode);
        int index;
        for (index=64; index<stringBuffer.length(); index+=65){
            stringBuffer.insert(index, "\n");
        }
        String pemCert = "-----BEGIN CERTIFICATE-----\r\n" + stringBuffer + "\r\n-----END CERTIFICATE-----\r\n";
        return pemCert;
    }

    /**
     * 反序列化证书
     * @param pemcert
     * @return
     */
    public static Certificate getCertByPem (String pemcert) {
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        Certificate cert = null;
        try {
            cert = cf.generateCertificate(new ByteArrayInputStream(pemcert.getBytes()));
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return cert;
    }

    /**
     * 将私钥转换成base64字符串
     * @param privateKey
     * @return
     */
    public static String getPemFromPrivateKey(PrivateKey privateKey) {
        String strCertificate = null;
        try {
            String encode = Base64Utils.encode2String(privateKey.getEncoded());
            strCertificate = "-----BEGIN PRIVATEKEY-----\r\n" + encode + "\r\n-----END PRIVATEKEY-----\r\n";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getPemFromPrivateKey:" + e.getMessage());
        }
        return Base64Utils.encode2String(strCertificate);
    }
    /**
     * pem得到私钥
     * @param content
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static PrivateKey loadECPrivateKey(String content,  String algorithm) throws Exception {
        String privateKeyPEM = content.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "").replace("", "");
        //byte[] asBytes = Base64Utils.decode(privateKeyPEM);
        byte[] asBytes = Base64Utils.decode2Bytes(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(asBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(spec);
    }

    /**
     * 加载公钥
     * @param content
     * @param algorithm
     * @return
     * @throws Exception
     */
    public static PublicKey loadECPublicKey(String content,  String algorithm) throws Exception {
        String strPublicKey = content.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "").replace("", "");
        //byte[] asBytes = Base64Utils.decode(strPublicKey);
        byte[] asBytes = Base64Utils.decode2Bytes(strPublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(asBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return (ECPublicKey) keyFactory.generatePublic(spec);
    }

    /**
     * 将Certificate序列化为pem格式后保存到文件系统文件
     * @param certificate
     * @param path
     */
    public void savePemCertAsFile(Certificate certificate, String path, String certName){
        String encodedCert = null;
        try {
            encodedCert = Base64Utils.encode2String(certificate.getEncoded());
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        File file = new File(path+"/"+certName+".cer");
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            randomAccessFile.write("-----BEGIN CERTIFICATE-----\n".getBytes());
            int i = 0;
            for (; i<(encodedCert.length() - (encodedCert.length() % 64)); i+=64) {
                randomAccessFile.write(encodedCert.substring(i, i + 64).getBytes());
                randomAccessFile.write("\n".getBytes());
            }
            randomAccessFile.write(encodedCert.substring(i, encodedCert.length()).getBytes());
            randomAccessFile.write("\n".getBytes());
            randomAccessFile.write("-----END CERTIFICATE-----".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成带密码的空jks
     * @param certInfo {cn,ou,o,c,l,st,starttime,endtime,serialnumber}
     * @param jks_password
     * @param path jks的存储路径（目录）
     */
    public void generateNullJKS(String[] certInfo, String jks_password, String path) {
        String jks_path = path+"/" + certInfo[0] + ".jks";
        File file = new File(jks_path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(null, jks_password.toCharArray());
            keyStore.store(new FileOutputStream(jks_path), jks_password.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将证书加入已有的空jks
     * @param cert
     * @param keyPair
     * @param jks_password
     * @param path
     */
    public void addCert2JKS(X509Certificate cert, KeyPair keyPair, String jks_password, String path, String commonName) {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(path+"/"+commonName+".jks"), jks_password.toCharArray());

            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = cert;
            keyStore.setCertificateEntry(commonName, cert);
            keyStore.setKeyEntry(commonName, keyPair.getPrivate(), jks_password.toCharArray(), chain);
            keyStore.store(new FileOutputStream(path+"/" + commonName + ".jks"), jks_password.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建jks,同时加入Certificate
     * @param cert
     * @param keyPair
     * @param jks_password
     * @param path
     * @param alias
     */
    public void generateJksWithCert(X509Certificate cert, KeyPair keyPair, String jks_password, String path, String alias){
        KeyStore keyStore;
        String jks_path = path + "/" + alias + ".jks";
        File file = new File(jks_path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            keyStore = KeyStore.getInstance("jks");
            keyStore.load(null, jks_password.toCharArray());
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = cert;
            keyStore.setCertificateEntry(alias, cert);
            keyStore.setKeyEntry(alias, keyPair.getPrivate(), jks_password.toCharArray(), chain);
            keyStore.store(new FileOutputStream(jks_path), jks_password.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密私钥
     * @param privateKey
     * @param password
     * @return
     */
    public static String encryptPrivateKey(PrivateKey privateKey, String password){
        String pemPrivateKey = CertificateUtils.getPemFromPrivateKey(privateKey);
        String encyptPrivateKey = null;
        try {
            encyptPrivateKey = AESCrypt.encrypt2String(pemPrivateKey, MD5Utils.crypt16Byte(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encyptPrivateKey;
    }

    public static PrivateKey decryptPrivateKey(String encryptPrivateKey, String password) throws Exception {
        String pemPrivateKey = AESCrypt.decrypt2String(encryptPrivateKey, MD5Utils.crypt16Byte(password));
        return CertificateUtils.loadECPrivateKey(pemPrivateKey, "EC");
    }

    /**
     * 读取私钥
     * @param encodedKey
     * @param algorithm
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey loadPrivateKey(byte[] encodedKey,  String algorithm)
            throws NoSuchAlgorithmException, InvalidKeySpecException{
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(keySpec);
    }


    /**
     * 从字符串中提取证书
     * @param cert
     * @return
     */
    public X509Certificate fromString(String cert) {
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            String strCertificate = "-----BEGIN CERTIFICATE-----\n" + cert + "\n-----END CERTIFICATE-----\n";
            java.io.ByteArrayInputStream streamCertificate = new java.io.ByteArrayInputStream(
                    strCertificate.getBytes("UTF-8"));
            return (X509Certificate) certificateFactory.generateCertificate(streamCertificate);
        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }
        return null;
    }

    /**
     *
     * @param keyPair
     * @param path
     */
    public void createPublicKeyByDecode(KeyPair keyPair, String path) {
        try {
            PublicKey publicKey = keyPair.getPublic();
            FileWriter wr = new java.io.FileWriter(new File(path));
            String encode = Base64Utils.encode2String(publicKey.getEncoded());
            String strCertificate = "-----BEGIN PUBLICKEY-----\r\n" + encode + "\r\n-----END PUBLICKEY-----\r\n";

            wr.write(strCertificate); // 给公钥编码
            wr.flush();
            wr.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("public key :" + e.getMessage());
        }
    }


    /**
     *
     * @param keyPair
     * @param path
     */
    public void createPrivateKey(KeyPair keyPair, String path) {
        try {
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            FileWriter fileWriter = new java.io.FileWriter(new File(path));
            String encode = Base64Utils.encode2String(privateKey.getEncoded());
            String strCertificate = "-----BEGIN PRIVATEKEY-----\r\n" + encode + "\r\n-----END PRIVATEKEY-----\r\n";

            fileWriter.write(strCertificate); // 给私钥编码
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("private key :" + e.getMessage());
        }
    }

    /**
     * 序列化公钥
     * @param publicKey
     * @param name
     * @throws Exception
     */
    public static void savePublicKeyAsPEM(PublicKey publicKey, String name) throws Exception {
        String content = Base64Utils.encode2String(publicKey.getEncoded());
        File file = new File(name);
        if ( file.isFile() && file.exists() )
            throw new IOException("file already exists");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            randomAccessFile.write("-----BEGIN PUBLIC KEY-----".getBytes());
            int i = 0;
            for (; i<(content.length() - (content.length() % 64)); i+=64) {
                randomAccessFile.write(content.substring(i, i + 64).getBytes());
                //randomAccessFile.write('');
                randomAccessFile.write("''".getBytes());
            }

            randomAccessFile.write(content.substring(i, content.length()).getBytes());
            randomAccessFile.write("''".getBytes());

            randomAccessFile.write("-----END PUBLIC KEY-----".getBytes());
        }
    }

    /**
     * 序列化私钥
     * @param privateKey
     * @param name
     * @throws Exception
     */
    public static void savePrivateKeyAsPEM(PrivateKey privateKey, String name) throws Exception {
        // String content = Base64.encode(privateKey.getEncoded());
        String content = Base64Utils.encode2String(privateKey.getEncoded());
        File file = new File(name);
        if ( file.isFile() && file.exists() )
            throw new IOException("file already exists");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            randomAccessFile.write("-----BEGIN PRIVATE KEY-----".getBytes());
            int i = 0;
            for (; i<(content.length() - (content.length() % 64)); i+=64) {
                randomAccessFile.write(content.substring(i, i + 64).getBytes());
                //randomAccessFile.write('');
                randomAccessFile.write("''".getBytes());
            }

            randomAccessFile.write(content.substring(i, content.length()).getBytes());
            //randomAccessFile.write('');
            randomAccessFile.write("''".getBytes());

            randomAccessFile.write("-----END PRIVATE KEY-----".getBytes());
        }
    }


    /**
     * 读取公钥, encodedKey为从文件中读取到的byte[]数组
     * @param encodedKey
     * @param algorithm
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey loadPublicKey(byte[] encodedKey, String algorithm)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(keySpec);
    }


    /**
     * readBytes代码
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(final InputStream inputStream) throws IOException {
        final int BUFFER_SIZE = 1024;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int readCount;
        byte[] data = new byte[BUFFER_SIZE];
        while ((readCount = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, readCount);
        }

        buffer.flush();
        return buffer.toByteArray();
    }
}
