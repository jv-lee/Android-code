package com.lee.code.structure.decorator.core;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 抽象装饰角色：继承抽象构件，并包含具体构件的实例，可以通过其子类扩展具体构件的功能
 */
public class Decorator implements Component {

    private Component component;

    public Decorator(Component component) {
        this.component = component;
    }

    @Override
    public void operation() {
        component.operation();
    }
}
