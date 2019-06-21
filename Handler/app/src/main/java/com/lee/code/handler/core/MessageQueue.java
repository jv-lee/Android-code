package com.lee.code.handler.core;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author jv.lee
 * @date 2019/6/21.
 * description：
 */
public class MessageQueue {

    /**
     * 阻塞队列
     */
    BlockingQueue<Message> blockingQueue = new ArrayBlockingQueue<>(50);

    /**
     * 将消息对象存入阻塞队列中
     * @param message
     */
    public void enqueueMessage(Message message) {
        try {
            blockingQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从消息队列中取出消息
     * @return
     */
    public Message next() {
        try {
            return blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
