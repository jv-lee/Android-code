package com.lee.code.livedatabus.livedata2;

import android.support.annotation.Nullable;

/**
 * @author jv.lee
 * @date 2019/3/30
 * 观察者
 */
public abstract class Observer<T> {

    /**
     * 活跃状态
     */
    static final int STATE_ACTIVE = 1;
    /**
     * 暂停状态
     */
    static final int STATE_PAUSE = 2;
    /**
     * 销毁状态
     */
    static final int STATE_DESTORY = 3;
    /**
     * 初始化状态
     */
    static final int STATE_INIT = 0;

    /**
     * 初始状态为 初始化状态
     */
    private int state = STATE_INIT;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * 组件状态
     * @param t
     */
    public abstract void onChanged(@Nullable T t);

}
