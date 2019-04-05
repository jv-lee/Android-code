package com.lee.library.livedatabus;

/**
 * @author jv.lee
 * @date 2019/3/30
 * 生命周期监听接口
 */
public interface LifecycleListener {

    /**
     * 绑定activity onCreate接口
     * @param activityCode activity路径地址
     */
    void onCreate(int activityCode);

    /**
     * 绑定activity onStart接口
     * @param activityCode activity路径地址
     */
    void onStart(int activityCode);

    /**
     * 绑定activity onStart接口
     * @param activityCode activity路径地址
     */
    void onPause(int activityCode);

    /**
     * 绑定activity onStart接口
     * @param activityCode activity路径地址
     */
    void onDetach(int activityCode);
}
