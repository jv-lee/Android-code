package com.lee.okhttprequset.chain2;

/**
 * @author jv.lee
 * @date 2019-08-28
 * @description
 */
public class Task1 implements IBaseTask {
    @Override
    public void doRunAction(String isTask, IBaseTask iBaseTask) {
        if ("no".equals(isTask)) {
            System.out.println("拦截器 任务节点一 处理了...");
        } else {
            //继续执行下一个链条的任务节点
            iBaseTask.doRunAction(isTask, iBaseTask);
        }
    }
}
