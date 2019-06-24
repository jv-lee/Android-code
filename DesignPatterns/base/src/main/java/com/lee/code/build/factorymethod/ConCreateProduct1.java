package com.lee.code.build.factorymethod;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 具体商品1
 */
public class ConCreateProduct1 implements Product {
    @Override
    public void show() {
        System.out.println("具体产品1显示...");
    }
}
