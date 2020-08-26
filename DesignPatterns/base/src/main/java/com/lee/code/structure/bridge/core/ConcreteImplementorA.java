package com.lee.code.structure.bridge.core;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 具体实现化角色
 */
public class ConcreteImplementorA implements Implementor {
    @Override
    public void OperationImpl() {
        System.out.println("具体实现化(Concrete Implementor)角色被访问");
    }
}
