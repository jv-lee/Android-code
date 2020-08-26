package com.lee.code.structure.adapter.core;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 类适配器模式
 */
public class ClassAdapter extends Adapter implements Target {
    @Override
    public void request() {
        specificRequest();
    }
}
