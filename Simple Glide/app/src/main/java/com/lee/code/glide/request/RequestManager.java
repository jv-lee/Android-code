package com.lee.code.glide.request;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

public class RequestManager {
    private static RequestManager instance;

    private RequestManager() {
        start();
    }

    public static RequestManager getInstance() {
        if (instance == null) {
            synchronized (RequestManager.class) {
                if (instance == null) {
                    instance = new RequestManager();
                }
            }
        }
        return instance;
    }

    //阻赛式队列 arraylist list .get()
    private LinkedBlockingQueue<BitmapRequest> requestQueue = new LinkedBlockingQueue<>();

    /*
    * 添加网络请求
    * */
    public void addBitmapRequest(BitmapRequest request) {
        //判断当前请求队列是否包含 不包含开始进入队列任务
        if (!requestQueue.contains(request)) {
            requestQueue.add(request);
        }else{
            Log.i("lee >>> ", "request存在队列中 ");
        }
    }

    //转发器
    private BitmapDispatcher[] dispatchers;

    //开启4个线程开启任务
    private void start() {
        stop();
        int threadCount = Runtime.getRuntime().availableProcessors();
        dispatchers = new BitmapDispatcher[threadCount];
        for (int i = 0; i < dispatchers.length; i++) {
            BitmapDispatcher bitmapDispatcher = new BitmapDispatcher(requestQueue);
            bitmapDispatcher.start();
            dispatchers[i] = bitmapDispatcher;
        }
    }

    //关闭线程队列
    private void stop() {
        if (dispatchers != null && dispatchers.length > 0) {
            for (BitmapDispatcher bitmapDispatcher : dispatchers) {
                if (!bitmapDispatcher.isInterrupted()) {
                    bitmapDispatcher.interrupt();
                }
            }
        }
    }

}
