package com.lee.code.structure.composite.part;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 抽象构件:物品
 */
public interface Articles {
    /**
     * 计算
     *
     * @return 价格
     */
    float calculation();

    void show();
}
