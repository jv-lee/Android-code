package com.gionee.gnservice.entity;

import java.io.Serializable;

/**
 * Created by caocong on 5/27/17.
 */
public class CouponInfo implements Serializable {
    public static final int TYPE_COUPON = 0;
    public static final int TYPE_GAME = 1;
    public static final int TYPE_THEME = 2;
    public static final int TYPE_MUSIC = 3;
    private int type;
    private String ano;
    private int denomination;
    private float balance;
    private String startTime;
    private String endTime;
    private String appName;
    private String taskId;
    private float useAmount;
    private String userAppName;
    private int status;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public int getDenomination() {
        return denomination;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public float getUseAmount() {
        return useAmount;
    }

    public void setUseAmount(float useAmount) {
        this.useAmount = useAmount;
    }

    public String getUserAppName() {
        return userAppName;
    }

    public void setUserAppName(String userAppName) {
        this.userAppName = userAppName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CouponInfo{" +
                "type='" + type + '\'' +
                ", ano='" + ano + '\'' +
                ", denomination=" + denomination +
                ", balance=" + balance +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", appName='" + appName + '\'' +
                ", taskId='" + taskId + '\'' +
                ", useAmount=" + useAmount +
                ", userAppName='" + userAppName + '\'' +
                ", status=" + status +
                '}';
    }
}
