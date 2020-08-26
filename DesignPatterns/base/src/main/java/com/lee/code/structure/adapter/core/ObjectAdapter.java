package com.lee.code.structure.adapter.core;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 对象适配器
 */
public class ObjectAdapter implements Target {
    private Adapter adapter;

    public ObjectAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void request() {
        adapter.specificRequest();
    }
}
