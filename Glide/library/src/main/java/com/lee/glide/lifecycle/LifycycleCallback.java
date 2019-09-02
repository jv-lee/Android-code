package com.lee.glide.lifecycle;

/**
 * @author jv.lee
 * @date 2019/9/2.
 * @description 生命周期监听
 */
public interface LifycycleCallback {
    /**
     * 生命周期初始化
     */
    void glideInitAction();

    /**
     * 生命周期停止了
     */
    void glideStopAction();

    /**
     * 生命周期释放
     */
    void glideRecycleAction();
}
