package com.lee.okhttprequset.chain2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019-08-28
 * @description
 */
public class ChainManager implements IBaseTask {

    private List<IBaseTask> iBaseTasks = new ArrayList<>();
    private int index = 0;

    public void addTask(IBaseTask iBaseTask) {
        iBaseTasks.add(iBaseTask);
    }

    @Override
    public void doRunAction(String isTask, IBaseTask iBaseTask) {
        if (iBaseTasks.isEmpty()) {
            //抛出异常...
            return;
        }

        //大于等于就代表到最后了 无需执行
        if (index >= iBaseTasks.size()) {
            return;
        }

        IBaseTask iBaseTaskResult = iBaseTasks.get(index);
        index++;
        iBaseTaskResult.doRunAction(isTask, iBaseTask);

    }
}
