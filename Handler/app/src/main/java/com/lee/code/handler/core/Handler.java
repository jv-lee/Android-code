package com.lee.code.handler.core;

/**
 * @author jv.lee
 * @date 2019/6/21.
 * description：
 */
public class Handler {

    private Looper mLooper;
    private MessageQueue mQueue;

    public Handler() {
        mLooper = Looper.myLooper();
        if (mLooper == null) {
            throw new RuntimeException(
                    "Can't create handler inside thread " + Thread.currentThread()
                            + " that has not called Looper.prepare()");
        }
        mQueue = mLooper.mQueue;
    }

    /**
     * 发送消息方法
     *
     * @param message
     */
    public void sendMessage(Message message) {
        //将消息放入队列
        enqueueMessage(message);
    }

    private void enqueueMessage(Message message) {
        //再发送消息进入队列之前 就将当前handler赋值到message中的target
        message.target = this;

        //使用mQueue将消息放入消息队列
        mQueue.enqueueMessage(message);
    }

    public void dispatchMessage(Message msg) {
        handleMessage(msg);
    }

    /**
     * 给开发者提供的开放API，用于重写和回调监听
     *
     * @param msg
     */
    public void handleMessage(Message msg) {
    }

}
