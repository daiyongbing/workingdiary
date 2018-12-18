package com.iscas.workingdiary.bean;

import java.io.Serializable;

public class CertInfo implements Serializable {
    private static final long serialVersionUID = -8788831257996777633L;
    private String CN;
    private String OU;
    private String O;
    private String C;
    private String L;
    private String ST;
    private String cPassword;

    public String getCN() {
        return CN;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    public String getOU() {
        return OU;
    }

    public void setOU(String OU) {
        this.OU = OU;
    }

    public String getO() {
        return O;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public void setO(String o) {
        O = o;
    }

    public String getL() {
        return L;
    }

    public void setL(String l) {
        L = l;
    }

    public String getST() {
        return ST;
    }

    public void setST(String ST) {
        this.ST = ST;
    }

    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }
}
