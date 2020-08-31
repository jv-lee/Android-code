package com.lee.code;

import com.lee.code.structure.composite.Component;
import com.lee.code.structure.composite.Composite;
import com.lee.code.structure.composite.Leaf;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description 组合模式测试案例
 */
public class CompositeTest {

    @Test
    public void test() {
        Composite c1 = new Composite();
        Composite c2 = new Composite();
        Component leaf1 = new Leaf("leaf-1");
        Component leaf2 = new Leaf("leaf-2");
        Component leaf3 = new Leaf("leaf-3");
        c1.add(leaf1);
        c1.add(c2);
        c2.add(leaf2);
        c2.add(leaf3);
        c1.operation();
    }
}
