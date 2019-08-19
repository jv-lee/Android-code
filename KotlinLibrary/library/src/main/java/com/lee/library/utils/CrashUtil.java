package com.lee.library.utils;

import android.content.Context;

/**
 * @author jv.lee
 * @date 2019/5/21.
 * description：
 */
public class CrashUtil implements Thread.UncaughtExceptionHandler {

    /**
     * 单例引用，这里我们做成单例的，因为我们一个应用程序里面只需要一个UncaughtExceptionHandler实例
     */
    private static CrashUtil instance;

    private CrashUtil() {
    }

    /**
     * 同步方法，以免单例多线程环境下出现异常
     *
     * @return
     */
    public synchronized static CrashUtil getInstance() {
        if (instance == null) {
            instance = new CrashUtil();
        }
        return instance;
    }

    /**
     * 初始化，把当前对象设置成UncaughtExceptionHandler处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {  //当有未处理的异常发生时，就会来到这里。。
        LogUtil.getStackTraceString(ex);
        if (mCrashCallback != null) {
            mCrashCallback.call(thread,ex);
        }
    }

    public interface CrashCallback{
        /**
         * 异常处理
         * @param thread
         * @param ex
         */
        void call(Thread thread, Throwable ex);
    }
    private CrashCallback mCrashCallback;

    public void setCrashCallback(CrashCallback crashCallback) {
        this.mCrashCallback = crashCallback;
    }
}
