package com.lee.okhttprequset.chain;

/**
 * @author jv.lee
 * @date 2019-08-28
 * @description
 */
public class Task4 extends BaseTask {

    public Task4(boolean isTask) {
        super(isTask);
    }

    @Override
    public void doAction() {
        //执行子节点 链条断开
        System.out.println(" Task4 任务节点四 执行了");
    }
}
