package com.lee.code;

import com.lee.code.behavioral.command.core.Command;
import com.lee.code.behavioral.command.core.ConcreteCommand;
import com.lee.code.behavioral.command.core.Invoker;
import com.lee.code.behavioral.command.part.AbstractCommand;
import com.lee.code.behavioral.command.part.CompositeInvoker;
import com.lee.code.behavioral.command.part.ConcreteCommand1;
import com.lee.code.behavioral.command.part.ConcreteCommand2;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 命令设计模式 测试案例
 */
public class CommandTest {
    @Test
    public void test() {
        //基础案例
        Command cmd = new ConcreteCommand();
        Invoker invoker = new Invoker(cmd);
        System.out.println("客户端访问调用者call方法");
        invoker.call();

        //应用场景
        AbstractCommand cmd1 = new ConcreteCommand1();
        AbstractCommand cmd2 = new ConcreteCommand2();
        CompositeInvoker ir = new CompositeInvoker();
        ir.add(cmd1);
        ir.add(cmd2);
        System.out.println("客户端访问调用者execute()方法...");
        ir.execute();
    }
}
