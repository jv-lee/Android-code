package com.gionee.gnservice.sdk.integral;

import android.os.AsyncTask;

import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.widget.AutoVerticalScrollTextView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by caocong on 5/4/17.
 */

public class TextUpDownScroll {
    private static final String TAG = TextUpDownScroll.class.getSimpleName();
    private static final int STOP_TIME = 3000;
    private WeakReference<AutoVerticalScrollTextView> mAutoScrollTextViewRef;
    private List<String> prizes;
    private volatile int index;
    private volatile boolean isRunning = false;
    private final Object mObject = new Object();

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
                    + "mTask size:" + e.getTaskCount());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory, sDiscardHandler);

    public TextUpDownScroll(AutoVerticalScrollTextView textView, List<String> prizes) {
        this.prizes = prizes;
        this.index = 0;
        mAutoScrollTextViewRef = new WeakReference<>(textView);
    }

    public void startScroll() {
        endScroll();
        if (!isRunning) {
            mTask.executeOnExecutor(THREAD_POOL_EXECUTOR);
            isRunning = true;
        }
    }

    private AsyncTask<Void, Void, Void> mTask = new AsyncTask<Void, Void, Void>() {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AutoVerticalScrollTextView tv = mAutoScrollTextViewRef.get();
            if (tv != null) {
                tv.setText(prizes.get(0));
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            AutoVerticalScrollTextView tv = mAutoScrollTextViewRef.get();
            if (tv == null) {
                return;
            }

            tv.next();
            index++;
            if (index >= prizes.size() - 1) {
                index = 0;
            }
            tv.setText(prizes.get(index));
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (isRunning) {
                try {
                    synchronized (mObject) {
                        mObject.wait(getStopTime());
                    }
                    publishProgress();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

    };

    public void endScroll() {
        isRunning = false;
        synchronized (mObject) {
            mObject.notifyAll();
        }
    }

    private long getStopTime() {
        return STOP_TIME;
    }
}
