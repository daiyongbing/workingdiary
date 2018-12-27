package com.iscas.workingdiary.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.sql.Timestamp;

public class Diary {
    private String diaryId;
    private String userName;
    private String diaryText;
    private String txId;
    @JSONField(format="yyyy-MM-dd")
    private Timestamp createTime;

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDiaryText() {
        return diaryText;
    }

    public void setDiaryText(String diaryText) {
        this.diaryText = diaryText;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
