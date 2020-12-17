package com.bnz.miaosha.entity;


import java.util.Date;

public class ActivityInterval {
    private Date theTime;
    private String status = "未开始";

    public Date getTheTime() {
        return theTime;
    }

    public void setTheTime(Date theTime) {
        this.theTime = theTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
