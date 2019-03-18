package com.lee.library.handler;

public class Handler {

    private MessageQueue mQueue;

    public Handler() {
        Looper mLooper = Looper.myLooper();
        mQueue = mLooper.mQueue;
    }

    /**
     * 发送消息，压入队列
     *
     * @param msg
     */
    public void sendMessage(Message msg) {
        msg.target = this;

        mQueue.enqueueMessage(msg);

    }

    /**
     * 处理消息
     *
     * @param msg
     */
    public void handleMessage(Message msg) {

    }


    /**
     * 分发消息
     *
     * @param msg
     */
    public void dispatchMessage(Message msg) {
        handleMessage(msg);
    }
}


