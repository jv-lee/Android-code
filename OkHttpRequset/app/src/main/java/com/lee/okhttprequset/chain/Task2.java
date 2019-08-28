package com.lee.okhttprequset.chain;

/**
 * @author jv.lee
 * @date 2019-08-28
 * @description
 */
public class Task2 extends BaseTask {

    public Task2(boolean isTask) {
        super(isTask);
    }

    @Override
    public void doAction() {
        //执行子节点 链条断开
        System.out.println(" Task2 任务节点二 执行了");
    }
}
