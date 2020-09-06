package com.lee.code.behavioral.tm.core;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 模板方法模式：抽象类
 */
public abstract class AbstractClass {
    /**
     * 模板方法
     */
    public void templateMethod() {
        specificMethod();
        abstractMethod1();
        abstractMethod2();
    }

    /**
     * 具体方法
     */
    public void specificMethod() {
        System.out.println("抽象类中的具体方法被调用");
    }

    /**
     * 抽象方法1
     */
    public abstract void abstractMethod1();

    /**
     * 抽象方法2
     */
    public abstract void abstractMethod2();
}
