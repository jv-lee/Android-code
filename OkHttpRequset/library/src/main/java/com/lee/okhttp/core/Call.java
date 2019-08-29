package com.lee.okhttp.core;

import java.io.IOException;

/**
 * @author jv.lee
 * @date 2019/8/29.
 * @description  请求线程控制接口
 */
public interface Call {

    /**
     * 异步任务
     * @param responseCallback 将要执行的异步线程请求任务
     */
    void enqueue(Callback responseCallback);

    /**
     * 同步任务
     * @return 服务器返回结构体
     * @throws IOException 错误异常
     */
    Response execute() throws IOException;

    void cancel();

    boolean isExecuted();

    boolean isCanceled();
}
