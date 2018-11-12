package com.iscas.workingdiary.bean;

/**
 * 证书实体类
 */
public class Cert {
    private String certNo;
    private int userId;
    private String pemCert;
    private String certAddr;
    private int certStatus;
    private int certLevel;

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPemCert() {
        return pemCert;
    }

    public void setPemCert(String pemCert) {
        this.pemCert = pemCert;
    }

    public String getCertAddr() {
        return certAddr;
    }

    public void setCertAddr(String certAddr) {
        this.certAddr = certAddr;
    }

    public int getCertStatus() {
        return certStatus;
    }

    public void setCertStatus(int certStatus) {
        this.certStatus = certStatus;
    }

    public int getCertLevel() {
        return certLevel;
    }

    public void setCertLevel(int certLevel) {
        this.certLevel = certLevel;
    }
}
