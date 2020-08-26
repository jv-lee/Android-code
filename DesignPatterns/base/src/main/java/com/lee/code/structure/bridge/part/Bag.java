package com.lee.code.structure.bridge.part;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 抽象化角色：包
 */
public abstract class Bag {
    protected Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public abstract String getName();
}
