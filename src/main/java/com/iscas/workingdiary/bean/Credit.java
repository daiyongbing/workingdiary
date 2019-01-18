package com.iscas.workingdiary.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.sql.Timestamp;

public class Credit implements Serializable {
    private static final long serialVersionUID = -4736298470017022092L;

    @JSONField(ordinal = 1)
    private int id;
    private String userName;
    private String diaryId;
    private String txid;
    private Integer score;
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Timestamp gainTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Timestamp getGainTime() {
        return gainTime;
    }

    public void setGainTime(Timestamp gainTime) {
        this.gainTime = gainTime;
    }
}
