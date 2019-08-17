package com.lee.app.mvp;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

/**
 * @author jv.lee
 * @date 2019/5/6
 */
public interface BaseLifecycleObserver extends LifecycleObserver {
    /**
     * Activity生命周期回调 onCreate
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCrete();

    /**
     * Activity生命周期回调 onStart
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart();

    /**
     * Activity生命周期回调 onResume
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume();

    /**
     * Activity生命周期回调 onPause
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause();

    /**
     * Activity生命周期回调 onStop
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop();

    /**
     * Activity生命周期回调 onDestroy
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy();
}
