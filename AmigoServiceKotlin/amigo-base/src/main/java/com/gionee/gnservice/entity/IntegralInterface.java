package com.gionee.gnservice.entity;

import java.io.Serializable;

/**
 * Created by jing on 17-9-15.
 */

public class IntegralInterface implements Serializable {
    private int integralValue;
    private int rankValue;
    private int growthValue;

    public int getIntegralValue() {
        return integralValue;
    }

    public void setIntegralValue(int integralValue) {
        this.integralValue = integralValue;
    }

    public int getRankValue() {
        return rankValue;
    }

    public void setRankValue(int rankValue) {
        this.rankValue = rankValue;
    }

    public int getGrowthValue() {
        return growthValue;
    }

    public void setGrowthValue(int growthValue) {
        this.growthValue = growthValue;
    }

    @Override
    public String toString() {
        return "IntegralInterface{" +
                "integralValue='" + integralValue + '\'' +
                ", rankValue='" + rankValue + '\'' +
                ", growthValue=" + growthValue +
                "}";
    }
}
