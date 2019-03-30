package com.lee.code.okhttp.network;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 网络任务线程池管理类
 */
public class ThreadPoolManager {

    private static ThreadPoolManager instance;

    public static ThreadPoolManager getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolManager.class) {
                if (instance == null) {
                    instance = new ThreadPoolManager();
                }
            }
        }
        return instance;
    }

    private ThreadPoolManager() {
        //核心线程数 最大的线程数 线程保持时间 TimeUnit.SECONDS秒  根据业务调整线程池参数
        mThreadPoolExecutor = new ThreadPoolExecutor(3, 10, 15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new RejectedExecutionHandler() {
            //判断任务是否被拒绝，抛出到当前rejectedExecution方法
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //处理抛出的任务
                addTask(r); //重新添加
            }
        });
        //执行轮询线程
        mThreadPoolExecutor.execute(communicateThread);
        mThreadPoolExecutor.execute(delayThread);
    }

    //1.创建队列 ，保存异步请求任务
    private LinkedBlockingDeque<Runnable> mQueue = new LinkedBlockingDeque<>(); //该队列的特性 是一个基于连接节点 fi fo  先进先出的一个方式排列

    //2.添加异步任务到队列中
    public void addTask(Runnable runnable) {
        if (runnable != null) {
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //3.创建线程池 在构造方法中创建
    private ThreadPoolExecutor mThreadPoolExecutor;

    //4.创建队列与线程池交互线程
    public Runnable communicateThread = new Runnable() {
        @Override
        public void run() {
            Runnable runn = null;
            while (true) {
                try {
                    runn = mQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mThreadPoolExecutor.execute(runn);
            }
        }
    };

    //5.创建延迟队列
    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();

    public void addDelayTask(HttpTask task) {
        if (task != null) {
            task.setDelayTime(3000);
            mDelayQueue.offer(task);
        }
    }

    public Runnable delayThread = new Runnable() {
        @Override
        public void run() {
            HttpTask task = null;
            while (true) {
                try {
                    task = mDelayQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (task.getRetryCount() < 3) {
                    mThreadPoolExecutor.execute(task);
                    task.setRetryCount(task.getRetryCount() + 1);
                    Log.e("lee - >>>", "重试机制 执行次数"+task.getRetryCount());
                }else{
                    Log.e("lee - >>> ", "重试机制 执行次数超限,放弃");
                }
            }
        }
    };


}
