package com.lee.code.structure.composite.core;

import com.lee.code.core.Tree;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 组合模式：抽象构件
 */
public interface Component extends Tree<Component> {
    void operation();
}
