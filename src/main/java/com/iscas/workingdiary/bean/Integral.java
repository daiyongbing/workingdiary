package com.iscas.workingdiary.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Integral implements Serializable {
    private static final long serialVersionUID = -4736298470017022092L;
    private int id;
    private String userName;
    private String diaryId;
    private String tx_id;
    private Integer score;
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

    public String getTx_id() {
        return tx_id;
    }

    public void setTx_id(String tx_id) {
        this.tx_id = tx_id;
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
