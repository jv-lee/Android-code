package com.lee.code.behavioral.command.core;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 命令模式：具体命令
 */
public class ConcreteCommand implements Command {

    private Receiver receiver;

    public ConcreteCommand() {
        this.receiver = new Receiver();
    }

    @Override
    public void execute() {
        receiver.action();
    }
}
