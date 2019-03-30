package com.lee.library.livedatabus;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
    public void observe(Activity activity, Observer<T> observer) {
        map.put(activity.hashCode(), observer);

        FragmentManager fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
        HolderFragment current = (HolderFragment) fragmentManager.findFragmentByTag(LIFECYCLE_TARGET);
        if (current == null) {
            current = new HolderFragment();
            fragmentManager.beginTransaction().add(current, LIFECYCLE_TARGET).commitAllowingStateLoss();
        }
        //监听fragment生命周期
        current.setLifecycleListener(new LifecycleListener() {
            @Override
            public void onCreate(int activityCode) {
                map.get(activityCode).setState(Observer.STATE_INIT);
            }

            @Override
            public void onStart(int activityCode) {
                map.get(activityCode).setState(Observer.STATE_ACTIVE);

                //判断当前是否有延迟消息 有消息则回调延迟消息
                if (mPendingDelayList.get(activityCode) != null && mPendingDelayList.get(activityCode).size() > 0) {
                    for (T t : mPendingDelayList.get(activityCode)) {
                        map.get(activityCode).onChanged(t);
                    }
                }
                //处理完毕清除消息列表
                mPendingDelayList.clear();
            }

            @Override
            public void onPause(int activityCode) {
                map.get(activityCode).setState(Observer.STATE_PAUSE);
            }

            @Override
            public void onDetach(int activityCode) {
                map.remove(activityCode);
            }
        });
    }

}
