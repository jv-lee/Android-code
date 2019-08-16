package com.gionee.gnservice.entity;

import java.io.Serializable;

/**
 * Created by caocong on 5/31/17.
 */
public class IntegralRecord implements Serializable {
    private int score;
    private String createTime;
    private String action;
    private String appName;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "IntegralRecord{" +
                "score=" + score +
                ", createTime='" + createTime + '\'' +
                ", action='" + action + '\'' +
                ", appName='" + appName + '\'' +
                '}';
    }
}
