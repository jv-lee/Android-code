package com.lee.library.tool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具
 * @author jv.lee
 * @date 2019/4/6
 */
public class ThreadTool {

    private static ThreadTool instance;
    /**
     * 线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;

    public static ThreadTool getInstance(){
        if (instance == null) {
            synchronized (ThreadTool.class) {
                if (instance == null) {
                    instance = new ThreadTool();
                }
            }
        }
        return instance;
    }

    private ThreadTool(){
        //最大核心线程数、最大线程数、闲置时间、任务队列、工厂
        threadPoolExecutor = new ThreadPoolExecutor(3,
                Integer.MAX_VALUE,
                15,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r);
                    }
                });
    }

    public void addTask(Runnable runnable){
        threadPoolExecutor.execute(runnable);
    }

    public void removeTask(Runnable runnable) {
        threadPoolExecutor.remove(runnable);
    }

    public ThreadPoolExecutor getExecutor(){
        if (threadPoolExecutor != null) {
            return threadPoolExecutor;
        }
        return null;
    }


}
