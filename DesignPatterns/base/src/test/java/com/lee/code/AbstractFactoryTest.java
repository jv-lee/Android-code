package com.lee.code;

import com.lee.code.build.abstract_factory.impl.AntaFarm;
import com.lee.code.build.abstract_factory.impl.NikeFarm;
import com.lee.code.build.abstract_factory.interfaces.Farm;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 抽象工厂测试案例
 */
public class AbstractFactoryTest {

    @Test
    public void test() {
        Farm farm;
        farm = new NikeFarm();
        farm.newAnimal().show();
        farm.newPlant().show();

        farm = new AntaFarm();
        farm.newAnimal().show();
        farm.newPlant().show();
    }
}
