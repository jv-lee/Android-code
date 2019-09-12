package com.lee.library.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author jv.lee
 * @date 2019/9/11.
 * @description 定时任务, 重复任务 执行工具
 */
public class TimerEx {

    private static TimerEx instance;

    private ScheduledExecutorService timer;

    private TimerEx() {
        timer = Executors.newScheduledThreadPool(1);
    }

    public static TimerEx get() {
        if (instance == null) {
            synchronized (TimerEx.class) {
                if (instance == null) {
                    instance = new TimerEx();
                }
            }
        }
        return instance;
    }

    public ScheduledFuture<?> run(Runnable runnable, long time) {
        return timer.schedule(runnable, time, TimeUnit.SECONDS);
    }

    public ScheduledFuture<?> run(Runnable runnable, long delay, TimeUnit unit) {
        return timer.schedule(runnable, delay, unit);
    }

}
