package com.lee.code.build.abstract_factory.impl;

import com.lee.code.build.abstract_factory.interfaces.Animal;
import com.lee.code.build.abstract_factory.interfaces.Farm;
import com.lee.code.build.abstract_factory.interfaces.Plant;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 具体工程：Anta农场
 */
public class AntaFarm implements Farm {

    public AntaFarm() {
        System.out.println("Anta农场创建成功");
    }

    @Override
    public Animal newAnimal() {
        System.out.println("新马出生~");
        return new Horse();
    }

    @Override
    public Plant newPlant() {
        System.out.println("水果长成~");
        return new Fruitage();
    }
}
