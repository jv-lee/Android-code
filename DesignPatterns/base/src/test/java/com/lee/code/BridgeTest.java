package com.lee.code;

import com.lee.code.structure.bridge.core.Abstraction;
import com.lee.code.structure.bridge.core.ConcreteImplementorA;
import com.lee.code.structure.bridge.core.Implementor;
import com.lee.code.structure.bridge.core.RefinedAbstraction;
import com.lee.code.structure.bridge.part.Bag;
import com.lee.code.structure.bridge.part.Color;
import com.lee.code.structure.bridge.part.HandBag;
import com.lee.code.structure.bridge.part.Red;
import com.lee.code.structure.bridge.part.Wallet;
import com.lee.code.structure.bridge.part.Yellow;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 桥接模式测试案例
 */
public class BridgeTest {
    @Test
    public void test() {
        //案例1
        Implementor implementor = new ConcreteImplementorA();
        Abstraction abstraction = new RefinedAbstraction(implementor);
        abstraction.Operation();

        //案例2
        Color color;
        Bag bag;
        if (true) {
            color = new Red();
            bag = new Wallet();
        } else {
            color = new Yellow();
            bag = new HandBag();
        }
        bag.setColor(color);
        System.out.println("这是一个" + bag.getName());
    }
}
