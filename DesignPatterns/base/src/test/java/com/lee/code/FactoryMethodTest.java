package com.lee.code;

import com.lee.code.build.factorymethod.AbstractFactory;
import com.lee.code.build.factorymethod.ConCreateFactory1;
import com.lee.code.build.factorymethod.ConCreateFactory2;
import com.lee.code.build.factorymethod.Product;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description
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
