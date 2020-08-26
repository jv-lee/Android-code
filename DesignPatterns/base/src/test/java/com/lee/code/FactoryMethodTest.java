package com.lee.code;

import com.lee.code.build.factory_method.AbstractFactory;
import com.lee.code.build.factory_method.ConCreateFactory1;
import com.lee.code.build.factory_method.ConCreateFactory2;
import com.lee.code.build.factory_method.Product;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 方法工厂测试案例
 */
public class FactoryMethodTest {

    @Test
    public void test() {
        //
        AbstractFactory factory1 = new ConCreateFactory1();
        Product product1 = factory1.newProduct();
        product1.show();

        AbstractFactory factory2 = new ConCreateFactory2();
        Product product2 = factory2.newProduct();
        product2.show();
    }
}
