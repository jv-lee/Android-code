package com.lee.code.build.abstract_factory.impl;

import com.lee.code.build.abstract_factory.interfaces.Plant;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 具体产品：蔬菜
 */
public class Vegetables implements Plant {

    @Override
    public void show() {
        System.out.println("这里是一个蔬菜~");
    }
}
