package com.lee.okhttprequset.chain2;

/**
 * @author jv.lee
 * @date 2019-08-28
 * @description
 */
public interface IBaseTask {

    /**
     *
     * @param isTask 是否有能力执行任务节点
     * @param iBaseTask 下一个任务节点
     */
    public void doRunAction(String isTask,IBaseTask iBaseTask);
}
