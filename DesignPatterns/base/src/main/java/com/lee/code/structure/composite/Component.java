package com.lee.code.structure.composite;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 抽象构件
 */
public interface Component {
    void add(Component component);

    void remove(Component component);

    Component getChild(int index);

    void operation();
}
