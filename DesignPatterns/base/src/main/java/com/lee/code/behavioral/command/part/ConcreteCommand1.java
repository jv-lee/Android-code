package com.lee.code.behavioral.command.part;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 树叶构件：具体命令1
 */
public class ConcreteCommand1 implements AbstractCommand {

    private CompositeReceiver receiver;

    public ConcreteCommand1() {
        this.receiver = new CompositeReceiver();
    }

    @Override
    public void execute() {
        receiver.action1();
    }
}
