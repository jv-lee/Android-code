package com.gionee.gnservice.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by caocong on 2/7/17.
 */
public enum MemberLevel implements Serializable {
    NORMAL(10), GOLD(20), PLATINUM(30), DIAMOND(40), BLACK_GOLD(50);
    private int value;

    MemberLevel(int value) {
        this.value = value;
    }

    private int growthValue;

    private List<MemberLevelInfo> memberLevelInfos;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static MemberLevel get(int value) {
        switch (value) {
            case 10:
                return NORMAL;
            case 20:
                return GOLD;
            case 30:
                return PLATINUM;
            case 40:
                return DIAMOND;
            case 50:
                return BLACK_GOLD;
            default:
                return NORMAL;
        }
    }

    @Override
    public String toString() {
        return "MemberLevel{" +
                "value=" + value +
                ", growthValue=" + growthValue +
                ", memberLevelInfos=" + memberLevelInfos +
                '}';
    }

    public int getGrowthValue() {
        return growthValue;
    }

    public void setGrowthValue(int growthValue) {
        this.growthValue = growthValue;
    }

    public List<MemberLevelInfo> getMemberLevelInfos() {
        return memberLevelInfos;
    }

    public void setMemberLevelInfos(List<MemberLevelInfo> memberLevelInfos) {
        this.memberLevelInfos = memberLevelInfos;
    }
}
