package com.lee.code.behavioral.strategy;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description
 */
public class ConcreteStrategyB implements Strategy {
    @Override
    public void strategyMethod() {
        System.out.println("具体策略B的策略方法被访问!");
    }
}
