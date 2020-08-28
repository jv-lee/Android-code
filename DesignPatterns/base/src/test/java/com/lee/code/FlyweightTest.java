package com.lee.code;

import com.lee.code.structure.flyweight.Flyweight;
import com.lee.code.structure.flyweight.FlyweightFactory;
import com.lee.code.structure.flyweight.UnsharedConcreteFlyweight;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2020/8/28.
 * @description 亨元模式用例
 */
public class FlyweightTest {
    @Test
    public void test() {
        FlyweightFactory factory=new FlyweightFactory();
        Flyweight f01=factory.getFlyweight("a");
        Flyweight f02=factory.getFlyweight("a");
        Flyweight f03=factory.getFlyweight("a");
        Flyweight f11=factory.getFlyweight("b");
        Flyweight f12=factory.getFlyweight("b");
        f01.operation(new UnsharedConcreteFlyweight("第1次调用a。"));
        f02.operation(new UnsharedConcreteFlyweight("第2次调用a。"));
        f03.operation(new UnsharedConcreteFlyweight("第3次调用a。"));
        f11.operation(new UnsharedConcreteFlyweight("第1次调用b。"));
        f12.operation(new UnsharedConcreteFlyweight("第2次调用b。"));
    }
}