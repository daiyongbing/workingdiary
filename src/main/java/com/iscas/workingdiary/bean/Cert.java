package com.iscas.workingdiary.bean;

import java.io.FileInputStream;
import java.sql.Timestamp;

/**
 * 证书实体类
 */
public class Cert {
    private String certNo;
    private String pemCert;
    private String certAddr;
    private String certStatus;
    private String certLevel;
    private String jksString;
    private Timestamp createTime;

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
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

    public String getCertStatus() {
        return certStatus;
    }

    public void setCertStatus(String certStatus) {
        this.certStatus = certStatus;
    }

    public String getCertLevel() {
        return certLevel;
    }

    public void setCertLevel(String certLevel) {
        this.certLevel = certLevel;
    }

    public String getJksString() {
        return jksString;
    }

    public void setJksString(String jksString) {
        this.jksString = jksString;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
