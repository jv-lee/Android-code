package com.lee.okhttprequset;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jv.lee
 * @date 2019-08-28
 * @description
 */
public class MyThreadPool {

    @Test
    public void test() {

        /**
         * todo 参数一：corePoolSize
         * todo 参数二： maximumPoolSize
         * todo 参数三/四： 时间数值keepAliveTime , 单位：时分秒 60s
         *                          正在执行的任务Runnable20 < corePoolSize  -- 参数三/四才会起作用
         *                          作用：Runnable1执行完毕后，闲置60s，如果过了闲置60s，会回收掉Runnable1任务，如果在闲置时间60s 复用此线程 Runnable1
         * todo 参数五：workQueue 队列 ：会把超出的任务加入到队列中 缓存起来
         */
//        ExecutorService executorService =
//                new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        //无限复用同一个线程 ， 线程池缓存策略
        ExecutorService executorService =
                new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

        for (int i = 0; i < 20; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        System.out.println("当前线程，执行耗时任务，线程是：" + Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        System.out.println("打印日志");

        //缓存策略 0 Integer.MAX_VALUE
        Executors.newCachedThreadPool();

        //只有一个核心线程 最大线程数也是一个 就是单列线程池
        Executors.newSingleThreadExecutor();

        //固定线程池创建，  5个核心 5个最大线程数
        Executors.newFixedThreadPool(5);

    }

}
