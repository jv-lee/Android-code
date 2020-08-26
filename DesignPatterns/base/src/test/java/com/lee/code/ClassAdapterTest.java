package com.lee.code;

import com.lee.code.structure.adapter.core.ClassAdapter;
import com.lee.code.structure.adapter.core.Target;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author jv.lee
 * @date 2020/8/26.
 * @description 类适配器测试案例
 */
public class ClassAdapterTest {
    @Test
    public void test() {
        Target target = new ClassAdapter();
        target.request();
    }
}