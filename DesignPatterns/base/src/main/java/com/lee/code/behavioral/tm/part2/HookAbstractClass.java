package com.lee.code.behavioral.tm.part2;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 在模板方法模式中，基本方法包含：抽象方法、具体方法和钩子方法，正确使用“钩子方法”可以使得子类控制父类的行为。
 */
public abstract class HookAbstractClass {

    /**
     * 模板方法
     */
    public void templateMethod() {
        abstractMethod1();
        hookMethod1();
        if (hookMethod2()) {
            specificMethod();
        }
        abstractMethod2();
    }

    public void specificMethod() {
        System.out.println("抽象类中具体方法被调用");
    }

    public void hookMethod1() {
    }

    public boolean hookMethod2() {
        return true;
    }

    public abstract void abstractMethod1();

    public abstract void abstractMethod2();
}
