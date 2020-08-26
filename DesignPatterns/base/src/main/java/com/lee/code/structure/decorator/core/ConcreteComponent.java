package com.lee.code.structure.decorator.core;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 具体构件角色：实现抽象构件，通过装饰角色为其添加一些职责。
 */
public class ConcreteComponent implements Component {
    public ConcreteComponent() {
        System.out.println("创建具体构建角色");
    }

    @Override
    public void operation() {
        System.out.println("调用具体构建角色的方法operation()");
    }
}
