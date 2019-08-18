package com.lee.eventbus.model;

/**
 * @author jv.lee
 * @date 2019-08-18
 * @description
 */
public class ProductInfo {
    private String name;
    private double price;

    public ProductInfo(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
