package com.lee.code.structure.flyweight;

/**
 * @author jv.lee
 * @date 2020/8/28
 * @description 抽象亨元角色
 */
public interface Flyweight {
    void operation(UnsharedConcreteFlyweight state);
}
