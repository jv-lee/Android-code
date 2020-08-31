package com.lee.code.behavioral.command.part;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description 树叶构件：具体命令2
 */
public class ConcreteCommand2 implements AbstractCommand {

    private CompositeReceiver receiver;

    public ConcreteCommand2() {
        this.receiver = new CompositeReceiver();
    }

    @Override
    public void execute() {
        receiver.action2();
    }
}
