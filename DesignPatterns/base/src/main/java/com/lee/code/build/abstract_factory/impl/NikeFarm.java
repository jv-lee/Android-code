package com.lee.code.build.abstract_factory.impl;

import com.lee.code.build.abstract_factory.interfaces.Animal;
import com.lee.code.build.abstract_factory.interfaces.Farm;
import com.lee.code.build.abstract_factory.interfaces.Plant;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 具体工厂：Nick农场
 */
public class NikeFarm implements Farm {

    public NikeFarm() {
        System.out.println("Nike农场创建成功");
    }

    @Override
    public Animal newAnimal() {
        System.out.println("新牛出生~");
        return new Cattle();
    }

    @Override
    public Plant newPlant() {
        System.out.println("蔬菜长成~");
        return new Vegetables();
    }
}
