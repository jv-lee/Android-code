package com.lee.library;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jv.lee
 * @date 2019/9/28.
 * @description 上游被观察者 异步线程子类
 */
public class ObservableOnIo<T> implements ObservableOnSubscribe<T> {
    private final static ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private ObservableOnSubscribe<T> source;

    public ObservableOnIo(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    public void subscribe(@NonNull final Observer<T> emitter) throws Exception {
        EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    source.subscribe(emitter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
