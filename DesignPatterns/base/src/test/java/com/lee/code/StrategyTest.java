package com.lee.code;

import com.lee.code.behavioral.strategy.ConcreteStrategyA;
import com.lee.code.behavioral.strategy.ConcreteStrategyB;
import com.lee.code.behavioral.strategy.Context;
import com.lee.code.behavioral.strategy.Strategy;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 策略设计模式 测试案例
 */
public class StrategyTest {
    @Test
    public void test(){
        Context c=new Context();
        Strategy s=new ConcreteStrategyA();
        c.setStrategy(s);
        c.strategyMethod();
        System.out.println("-----------------");
        s=new ConcreteStrategyB();
        c.setStrategy(s);
        c.strategyMethod();
    }
}
