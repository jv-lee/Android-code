package com.lee.code.behavioral.tm.core;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description
 */
public class ConcreteClass extends AbstractClass {
    @Override
    public void abstractMethod1() {
        System.out.println("抽象方法1的实现被调用");
    }

    @Override
    public void abstractMethod2() {
        System.out.println("抽象方法2的实现被调用");
    }
}
