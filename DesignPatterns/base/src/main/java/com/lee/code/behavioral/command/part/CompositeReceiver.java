package com.lee.code.behavioral.command.part;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 接收者
 */
public class CompositeReceiver {
    public void action1() {
        System.out.println("接收者的action1()方法被调用...");
    }

    public void action2() {
        System.out.println("接收者的action2()方法被调用...");
    }
}
