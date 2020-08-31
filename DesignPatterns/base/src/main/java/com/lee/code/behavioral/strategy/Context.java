package com.lee.code.behavioral.strategy;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 环境类
 */
public class Context {
    private Strategy strategy;

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void strategyMethod() {
        strategy.strategyMethod();
    }
}
