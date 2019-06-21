package com.lee.code.handler.core;

/**
 * @author jv.lee
 * @date 2019/6/21.
 * description：
 */
public class Looper {

    public MessageQueue mQueue;
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<>();

    private Looper() {
        mQueue = new MessageQueue();
    }

    public static void prepare() {
        //首次进入 主线程只有一个Looper对象
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be create par thread");
        }

        sThreadLocal.set(new Looper());
    }

    public static Looper myLooper() {
        return sThreadLocal.get();
    }

    /**
     * 轮询提取消息
     */
    public static void loop() {
        //从全局ThreadLocalMap中获取唯一的Looper对象
        Looper me = myLooper();
        //从Looper对象中获取全局唯一消息队列对象 MessageQueue
        final MessageQueue queue = me.mQueue;

        Message resultMessage;
        while (true) {
            Message msg = queue.next();
            if (msg != null) {
                if (msg.target != null) {
                    msg.target.dispatchMessage(msg);
                }
            }
        }
    }
}
