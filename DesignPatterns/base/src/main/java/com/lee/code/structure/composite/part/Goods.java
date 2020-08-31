package com.lee.code.structure.composite.part;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description
 */
public class Goods implements Articles {

    //商品名
    private String name;
    //数量
    private int quantity;
    //单价
    private float unitPrice;

    public Goods(String name, int quantity, float unitPrice) {
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    @Override
    public float calculation() {
        return quantity * unitPrice;
    }

    @Override
    public void show() {
        System.out.println(name + "(quantity:" + quantity + ",unitPrice:" + unitPrice + "$");
    }
}
