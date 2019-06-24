package com.lee.code.build.prototype.manager;

/**
 * @author jv.lee
 * @date 2019/6/24.
 * @description
 */
public interface Shape extends Cloneable {
    /**
     * 原型拷贝方法
     *
     * @return 图形对象
     */
    Object clone();

    /**
     * 原型计算面积方法
     * @param number  数值
     */
    void countArea(int number);
}
