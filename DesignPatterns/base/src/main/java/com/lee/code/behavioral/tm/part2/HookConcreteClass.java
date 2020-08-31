package com.lee.code.behavioral.tm.part2;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 模板方法模式 钩子方法具体实现
 */
public class HookConcreteClass extends HookAbstractClass {
    @Override
    public void abstractMethod1() {
        System.out.println("抽象方法1实现被调用");
    }

    @Override
    public void abstractMethod2() {
        System.out.println("抽象方法2实现被调用");
    }

    @Override
    public void hookMethod1() {
        System.out.println("钩子方法1被重写");
    }

    @Override
    public boolean hookMethod2() {
        return false;
    }
}
