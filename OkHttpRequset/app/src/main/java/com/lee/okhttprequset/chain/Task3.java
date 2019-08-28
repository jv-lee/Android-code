package com.lee.okhttprequset.chain;

/**
 * @author jv.lee
 * @date 2019-08-28
 * @description
 */
public class Task3 extends BaseTask {

    public Task3(boolean isTask) {
        super(isTask);
    }

    @Override
    public void doAction() {
        //执行子节点 链条断开
        System.out.println(" Task3 任务节点三 执行了");
    }
}
