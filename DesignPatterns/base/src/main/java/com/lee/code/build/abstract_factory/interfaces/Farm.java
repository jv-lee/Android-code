package com.lee.code.build.abstract_factory.interfaces;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 抽象工厂:农场类
 */
public interface Farm {
    public Animal newAnimal();

    public Plant newPlant();
}
