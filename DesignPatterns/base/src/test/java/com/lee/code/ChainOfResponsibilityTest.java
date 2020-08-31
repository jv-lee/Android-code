package com.lee.code;

import com.lee.code.behavioral.cor.core.ConcreteHandler1;
import com.lee.code.behavioral.cor.core.ConcreteHandler2;
import com.lee.code.behavioral.cor.core.Handler;
import com.lee.code.behavioral.cor.part.ClassAdviser;
import com.lee.code.behavioral.cor.part.Dean;
import com.lee.code.behavioral.cor.part.DepartmentHead;
import com.lee.code.behavioral.cor.part.Leader;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 责任链模式测试案例
 */
public class ChainOfResponsibilityTest {

    @Test
    public void test() {
        //基础案例
        Handler handler1 = new ConcreteHandler1();
        Handler handler2 = new ConcreteHandler2();
        handler1.setNext(handler2);
        //提交请求
        handler1.handleRequest("two");

        //应用实例
        //组装责任链
        Leader teacher1=new ClassAdviser();
        Leader teacher2=new DepartmentHead();
        Leader teacher3=new Dean();
        //Leader teacher4=new DeanOfStudies();
        teacher1.setNext(teacher2);
        teacher2.setNext(teacher3);
        //teacher3.setNext(teacher4);
        //提交请求
        teacher1.handleRequest(8);
    }
}
