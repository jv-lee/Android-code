package com.lee.code.build.factorymethod;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description
 */
public interface AbstractFactory {
    /**
     * 获取抽象商品
     *
     * @return 商品
     */
    Product newProduct();
}
