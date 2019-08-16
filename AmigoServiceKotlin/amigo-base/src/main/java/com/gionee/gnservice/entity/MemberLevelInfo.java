package com.gionee.gnservice.entity;

import java.io.Serializable;

/**
 * Created by caocong on 5/25/17.
 */
public class MemberLevelInfo implements Serializable {
    private int minValue;
    private int maxValue;
    private String alisName;
    private int rank;

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public String getAlisName() {
        return alisName;
    }

    public void setAlisName(String alisName) {
        this.alisName = alisName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "MemberLevelInfo{" +
                "minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", alisName='" + alisName + '\'' +
                ", rank=" + rank +
                '}';
    }
}
