package com.lee.code.glide.lifecycle;

import android.content.Context;

import com.lee.code.glide.cache.DoubleLruCache;

public class LifecycleObservable {
    private static LifecycleObservable instance;

    public static LifecycleObservable getInstance(){
        if (instance == null) {
            synchronized (LifecycleObservable.class) {
                if (instance == null) {
                    instance = new LifecycleObservable();
                }
            }
        }
        return instance;
    }


    public void onStart() {
    }

    public void onStop() {
    }

    //回收
    public void onDestroy(int activityCode) {
        DoubleLruCache.getInstance().remove(activityCode);
    }

}
