package com.lee.okhttprequset.chain;

/**
 * @author jv.lee
 * @date 2019-08-28
 * @description 责任链父类
 */
public abstract class BaseTask {

    /**
     * 判断当前任务节点，有没有能力执行
     */
    private boolean isTask;

    public BaseTask(boolean isTask) {
        this.isTask = isTask;
    }

    /**
     * 执行写一个节点
     */
    private BaseTask nextTask;

    /**
     * 添加下一个节点任务
     *
     * @param nextTask 下一个任务
     */
    public void addNextTask(BaseTask nextTask) {
        this.nextTask = nextTask;
    }

    /**
     * 让自节点任务去完成
     */
    public abstract void doAction();

    public void action() {
        if (isTask) {
            //执行子节点 链条断开
            doAction();
        } else {
            //继续执行下一个任务节点
            if (nextTask != null) {
                nextTask.action();
            }
        }
    }
}
