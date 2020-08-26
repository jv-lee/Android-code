package com.lee.code;

import com.lee.code.structure.decorator.core.Component;
import com.lee.code.structure.decorator.core.ConcreteComponent;
import com.lee.code.structure.decorator.core.ConcreteDecorator;
import com.lee.code.structure.decorator.part.Girl;
import com.lee.code.structure.decorator.part.Morrigan;
import com.lee.code.structure.decorator.part.Original;
import com.lee.code.structure.decorator.part.Succubus;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 装饰模式测试案例
 */
public class DecoratorTest {
    @Test
    public void test() {
        //案例1
        Component component = new ConcreteComponent();
        component.operation();
        System.out.println(" - - - - - - - -- - - - - - - -");
        Component component1 = new ConcreteDecorator(component);
        component1.operation();

        //案例2
        Morrigan morrigan1 = new Original();
        morrigan1.display();

        Morrigan morrigan2 = new Succubus(morrigan1);
        morrigan2.display();

        Morrigan morrigan3 = new Girl(morrigan1);
        morrigan3.display();
    }
}
