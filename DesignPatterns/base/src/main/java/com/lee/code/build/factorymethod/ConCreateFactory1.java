package com.lee.code.build.factorymethod;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 具体工程1
 */
public class ConCreateFactory1 implements AbstractFactory {
    @Override
    public Product newProduct() {
        System.out.println("具体工厂1生产具体商品1->ConCreateProduct1");
        return new ConCreateProduct1();
    }
}
