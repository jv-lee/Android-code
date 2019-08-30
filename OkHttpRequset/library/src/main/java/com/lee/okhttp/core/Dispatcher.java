package com.lee.okhttp.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description 任务时间分发器
 */
public class Dispatcher {
    /**
     * 同时访问的任务最大限制值 64个任务
     */
    private int maxRequests = 64;

    /**
     * 同时访问同一个服务器域名最大限制 5个任务
     */
    private int maxRequestsPerHost = 5;

    /**
     * 存储运行队列
     */
    private Deque<RealCall.AsyncCall> runningAsyncCalls = new ArrayDeque<>();

    /**
     * 存储等待队列
     */
    private Deque<RealCall.AsyncCall> readyAsyncCalls = new ArrayDeque<>();

    public void enqueue(RealCall.AsyncCall call) {
        //同时运行的队列数 必须小于配置的64 && 同时访问同一个服务器域名不能超过5个
        if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
            runningAsyncCalls.add(call);
            executorService().execute(call);
        } else {
            readyAsyncCalls.add(call);
        }
    }

    /**
     * 获取缓存方案的线程池
     *
     * @return 线程池
     */
    private ExecutorService executorService() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("OKHttp simple threadPool");
                        thread.setDaemon(false);
                        return thread;
                    }
                });
    }

    /**
     * 判断AsyncCall中的Host，在运行的队列中计数，然后返回
     * 参数AsyncCall.Request.Host == runningAsyncCalls.for{AsyncCall.Request.Host} + 1
     *
     * @param call 异步任务
     * @return
     */
    private int runningCallsForHost(RealCall.AsyncCall call) {
        int count = 0;
        if (runningAsyncCalls.isEmpty()) {
            return 0;
        }

        SocketRequestServer srs = new SocketRequestServer();

        //遍历运行队列里面的所有任务，取出任务host == call.host +1 （判断当前请求任务，在运行中的请求任务总数）
        for (RealCall.AsyncCall runningAsyncCall : runningAsyncCalls) {
            if (srs.getHost(runningAsyncCall.getRequest()).equals(srs.getHost(call.getRequest()))) {
                count++;
            }

        }

        return count;
    }

    /**
     * 移除运行完成的任务
     * 把等待队列中所有的任务取出来执行 run -> AsyncCall.run finished
     * 任务回收
     * @param call 异步任务
     */
    void finished(RealCall.AsyncCall call) {
        //当前运行的任务 给回收
        runningAsyncCalls.remove(call);

        //等待队列里面是否有任务，如果有任务是需要执行的
        if (readyAsyncCalls.isEmpty()) {
            return;
        }

        //把等待队列中的任务给 移动 到运行队列
        for (RealCall.AsyncCall readyAsyncCall : readyAsyncCalls) {
            //删除等待队列
            readyAsyncCalls.remove(readyAsyncCall);

            //将删除的任务添加到运行队列
            runningAsyncCalls.add(readyAsyncCall);

            //开始执行任务
            executorService().execute(readyAsyncCall);
        }

    }

}
