package com.lee.okhttp.core;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description
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
}
