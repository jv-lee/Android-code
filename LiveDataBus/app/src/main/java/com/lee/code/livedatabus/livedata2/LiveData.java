package com.lee.code.livedatabus.livedata2;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019/3/30
 * 消息通道
 */
public class LiveData<T> {
    /**
     * Activity组件地址
     */
    private SparseArray<Observer<T>> map = new SparseArray<>();
    /**
     * 延迟消息集合
     */
    private SparseArray<List<T>> mPendingDelayList = new SparseArray<>();
    /**
     * 生命周期Fragment target值
     */
    private static final String LIFECYCLE_TARGET = "com.lee.code.livedata.lifecycle";
    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 其他线程切换
     * @param value
     */
    public void postValue(final T value) {
        synchronized (this) {
            handler.post(() -> setValue(value));
        }
    }

    /**
     * 发送消息
     * @param value
     */
    public void setValue(T value) {
        List<Observer> destoryList = new ArrayList<>();

        for (int i = 0; i < map.size(); i++) {
            Integer activityCode = map.keyAt(i);
            Observer<T> observer = map.get(activityCode);
            //活跃状态直接发送消息
            if (observer.getState() == Observer.STATE_ACTIVE) {
                observer.onChanged(value);
            }
            //不活跃状态
            if (observer.getState() == Observer.STATE_PAUSE) {
                //判断延迟消息列表是否为空
                if (mPendingDelayList.get(activityCode) == null) {
                    mPendingDelayList.put(activityCode,new ArrayList<>());
                }
                //消息不在延迟消息列表中 就添加进延迟消息列表
                if (!mPendingDelayList.get(activityCode).contains(value)) {
                    mPendingDelayList.get(activityCode).add(value);
                }
            }
            //销毁掉的
            if (observer.getState() == Observer.STATE_DESTORY) {
                destoryList.add(observer);
            }
        }
        Iterator<Observer> iterator = destoryList.iterator();
        while (iterator.hasNext()) {
            iterator.remove();
        }
    }

    /**
     * 绑定生命周期
     * @param activity
     * @param observer
     */
    public void observe(LifecycleOwner activity, Observer<T> observer) {
        map.put(activity.hashCode(), observer);

        //监听fragment生命周期
        activity.getLifecycle().addObserver(new LifecycleObserver() {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            void onCreate(@NonNull LifecycleOwner owner) {
                map.get(owner.hashCode()).setState(Observer.STATE_INIT);
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            void onStart(@NonNull LifecycleOwner owner) {
                map.get(owner.hashCode()).setState(Observer.STATE_ACTIVE);

                //判断当前是否有延迟消息 有消息则回调延迟消息
                if (mPendingDelayList.get(owner.hashCode()) != null && mPendingDelayList.get(owner.hashCode()).size() > 0) {
                    for (T t : mPendingDelayList.get(owner.hashCode())) {
                        map.get(owner.hashCode()).onChanged(t);
                    }
                }
                //处理完毕清除消息列表
                mPendingDelayList.clear();
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            void onPause(@NonNull LifecycleOwner owner) {
                map.get(owner.hashCode()).setState(Observer.STATE_PAUSE);

            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            void onDestroy(@NonNull LifecycleOwner owner) {
                map.remove(owner.hashCode());

            }

        });

    }

}
