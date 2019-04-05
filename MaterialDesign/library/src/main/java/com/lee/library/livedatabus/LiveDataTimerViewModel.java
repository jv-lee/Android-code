package com.lee.library.livedatabus;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jv.lee
 * @date 2019/3/30
 */
public class LiveDataTimerViewModel extends ViewModel {

    /**
     * 消息通道
     */
    private MutableLiveData<String> mTime = new MutableLiveData<>();
    private int i = 0;
    private ScheduledThreadPoolExecutor  scheduled;

    public LiveDataTimerViewModel(){
        scheduled = new ScheduledThreadPoolExecutor(2);
        scheduled.scheduleAtFixedRate(() -> {
            i++;
            mTime.postValue("lee >>> " + i);
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public MutableLiveData<String> getmTime() {
        return mTime;
    }

    public void stopTimer(){
        scheduled.shutdownNow();
        scheduled = null;
    }
}
