package com.lee.code.build.abstract_factory.impl;

import com.lee.code.build.abstract_factory.interfaces.Plant;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description 具体产品：水果类
 */
public class Fruitage implements Plant {

    public Fruitage() {
    }

    @Override
    public void show() {
        System.out.println("这里是水果噢 ~");
    }
}
