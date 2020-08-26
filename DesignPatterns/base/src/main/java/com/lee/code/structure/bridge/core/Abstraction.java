package com.lee.code.structure.bridge.core;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 抽象化角色
 */
public abstract class Abstraction {
    protected Implementor implementor;

    protected Abstraction(Implementor implementor) {
        this.implementor = implementor;
    }

    public abstract void Operation();
}
