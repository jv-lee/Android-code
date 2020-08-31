package com.lee.code;

import com.lee.code.behavioral.tm.AbstractClass;
import com.lee.code.behavioral.tm.ConcreteClass;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 模板方法设计模式测试案例
 */
public class TemplateMethod {
    @Test
    public void test() {
        //基础示范
        AbstractClass abstractClass = new ConcreteClass();
        abstractClass.templateMethod();
    }
}
