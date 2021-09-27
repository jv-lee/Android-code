package com.lee.library.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author jv.lee
 * @date 2019/9/11.
 * @description 定时任务器
 */
public class TimerUtil {

    private static TimerUtil instance;

    private ScheduledExecutorService timer;

    private TimerUtil() {
        timer = Executors.newScheduledThreadPool(1);
    }

    public static TimerUtil get() {
        if (instance == null) {
            synchronized (TimerUtil.class) {
                if (instance == null) {
                    instance = new TimerUtil();
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

    public void destroy() {
        timer.shutdownNow();
    }

}
