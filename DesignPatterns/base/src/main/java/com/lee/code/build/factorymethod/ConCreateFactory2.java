package com.lee.code.build.factorymethod;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 具体工厂2
 */
public class ConCreateFactory2 implements AbstractFactory {
    @Override
    public Product newProduct() {
        System.out.println("具体工厂1生产具体商品2->ConCreateProduct2");
        return new ConCreateProduct2();
    }
}
