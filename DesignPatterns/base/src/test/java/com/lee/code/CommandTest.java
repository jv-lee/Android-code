package com.lee.code;

import com.lee.code.behavioral.command.Command;
import com.lee.code.behavioral.command.ConcreteCommand;
import com.lee.code.behavioral.command.Invoker;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description
 */
public class CommandTest {
    @Test
    public void test() {
        Command cmd = new ConcreteCommand();
        Invoker invoker = new Invoker(cmd);
        System.out.println("客户端访问调用者call方法");
        invoker.call();
    }
}
