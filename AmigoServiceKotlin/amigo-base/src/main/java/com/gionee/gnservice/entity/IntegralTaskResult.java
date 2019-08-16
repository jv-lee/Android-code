package com.gionee.gnservice.entity;

import java.io.Serializable;

/**
 * 积分任务上传结果
 */
public class IntegralTaskResult implements Serializable {
    public static final int CODE_SCUCESS = 200;
    public static final int CODE_PARAM_ERROR = 400;//接口参数错误
    public static final int CODE_GET_ERROR = 600;//积分颁发失败
    private int id = -1;
    private int code;
    private String m;//预留字段，现在没用
    private String st;//预留字段，现在没用

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }


    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "IntegralTaskResult{" +
                "id=" + id +
                ", code=" + code +
                ", m='" + m + '\'' +
                ", st='" + st + '\'' +
                '}';
    }
}
