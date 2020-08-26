package com.lee.code.structure.decorator.core;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description
 */
public class ConcreteDecorator extends Decorator {
    public ConcreteDecorator(Component component) {
        super(component);
    }

    @Override
    public void operation() {
        super.operation();
        addedFunction();
    }

    public void addedFunction() {
        System.out.println("为具体构建角色增加额外附加的功能addedFunction()");
    }
}
