package com.lee.code.core;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 树形结构接口
 */
public interface Tree<T> {
    void add(T element);

    void remove(T element);

    T getChild(int index);
}
