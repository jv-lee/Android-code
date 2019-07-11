package com.lee.code.build.abstract_factory.impl;

import com.lee.code.build.abstract_factory.interfaces.Animal;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 具体产品：马
 */
public class Horse implements Animal {

    public Horse() {
    }

    @Override
    public void show() {
        System.out.println("我是一批马");
    }
}
