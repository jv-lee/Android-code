package com.lee.code.behavioral.command.core;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 命令模式：调用者
 */
public class Invoker {
    private Command command;

    public Invoker(Command command) {
        this.command = command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void call() {
        System.out.println("调用者执行命令command...");
        command.execute();
    }
}
