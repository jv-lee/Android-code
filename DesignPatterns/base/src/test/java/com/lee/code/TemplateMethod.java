package com.lee.code;

import com.lee.code.behavioral.tm.core.AbstractClass;
import com.lee.code.behavioral.tm.core.ConcreteClass;
import com.lee.code.behavioral.tm.part1.StudyAbroad;
import com.lee.code.behavioral.tm.part1.StudyInAmerica;
import com.lee.code.behavioral.tm.part2.HookAbstractClass;
import com.lee.code.behavioral.tm.part2.HookConcreteClass;

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

        //基础案例1
        StudyAbroad studyAbroad = new StudyInAmerica();
        studyAbroad.templateMethod();

        //基础案例2
        HookAbstractClass hookConcreteClass = new HookConcreteClass();
        hookConcreteClass.templateMethod();
    }
}
