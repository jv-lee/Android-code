package com.lee.code;

import com.lee.code.behavioral.cor.ConcreteHandler1;
import com.lee.code.behavioral.cor.ConcreteHandler2;
import com.lee.code.behavioral.cor.Handler;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 责任链模式测试案例
 */
public class ChainOfResponsibilityTest {

    @Test
    public void test() {
        Handler handler1 = new ConcreteHandler1();
        Handler handler2 = new ConcreteHandler2();
        handler1.setNext(handler2);
        //提交请求
        handler1.handleRequest("two");
    }
}
