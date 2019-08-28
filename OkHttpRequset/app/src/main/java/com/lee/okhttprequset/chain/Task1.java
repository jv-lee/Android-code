package com.lee.okhttprequset.chain;

/**
 * @author jv.lee
 * @date 2019-08-28
 * @description
 */
public class Task1 extends BaseTask {

    public Task1(boolean isTask) {
        super(isTask);
    }

    @Override
    public void doAction() {
        //执行子节点 链条断开
        System.out.println(" Task1 任务节点一 执行了");
    }
}
