package com.lee.code.structure.flyweight;

/**
 * @author jv.lee
 * @date 2020/8/28
 * @description 非亨元角色
 */
public class UnsharedConcreteFlyweight {
    private String info;

    public UnsharedConcreteFlyweight(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
