package com.lee.code.okhttp.network;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class HttpTask<T> implements Runnable,Delayed {

    private IHttpRequest mIHttpRequest;

    public HttpTask(String url,T requestData,IHttpRequest httpRequest,CallbackListener listener){
        mIHttpRequest = httpRequest;
        httpRequest.setUrl(url);
        httpRequest.setListener(listener);
        try {
            String content = new Gson().toJson(requestData);

            httpRequest.setData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            mIHttpRequest.execute();
        } catch (Exception e) {
            e.printStackTrace();
            //将失败的任务添加到重试队列
            ThreadPoolManager.getInstance().addDelayTask(this);
        }
    }

    private long delayTime;
    private int retryCount;

    public int getRetryCount(){
        return retryCount;
    }

    public void setRetryCount(int count) {
        this.retryCount = count;
    }

    public long getDelayTime(){
        return delayTime;
    }

    /**
     * 设置延迟时间
     * @param time
     */
    public void setDelayTime(long time) {
        this.delayTime = time + System.currentTimeMillis();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.delayTime - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
