package com.gionee.gnservice.domain;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by caocong on 5/24/17.
 */
public class Case {
    private static final String TAG = Case.class.getSimpleName();
    protected IAppContext mAppContext;
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 10;
    private static final int KEEP_ALIVE = 1;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);

    private static final RejectedExecutionHandler sDiscardHandler = new ThreadPoolExecutor.DiscardPolicy() {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            LogUtil.e(TAG, "core size : " + e.getCorePoolSize() + "max size:" + e.getMaximumPoolSize()
                    + "task size:" + e.getTaskCount());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory, sDiscardHandler);

    public Case(IAppContext appContext) {
        mAppContext = appContext;
    }

    protected <T> void execute(Observable<T> observable, Observer<T> observer) {
        try {
            PreconditionsUtil.checkNotNull(observable);
            PreconditionsUtil.checkNotNull(observer);
            observable.setObserver(observer);
            observable.executeOnExecutor(THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
