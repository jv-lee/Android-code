package com.lee.code;

import com.lee.code.structure.adapter.core.Adapter;
import com.lee.code.structure.adapter.core.ObjectAdapter;
import com.lee.code.structure.adapter.core.Target;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2020/8/26.
 * @description 对象适配器测试案例
 */
public class ObjectAdapterTest {
    @Test
    public void test() {
        Target target = new ObjectAdapter(new Adapter());
        target.request();
    }
}