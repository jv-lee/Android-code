package com.lee.code.handler;

import com.lee.code.handler.core.Handler;
import com.lee.code.handler.core.Looper;
import com.lee.code.handler.core.Message;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2019/6/21.
 * description：
 */
public class ActivityThread {

    @Test
    public void main() {
        //创建全局唯一的，主线程Looper对象，以及MessageQueue消息队列 在系统源码中 是由 ActivityThread main函数中调用 Looper.prepareMainLooper() -> prepare()
        Looper.prepare();

        //模拟Activity创建Handler对象
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println(msg.obj.toString());
            }
        };

        //消费消息 回调方法

        //子线程发送消息
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.obj = "hello handler ~";
                handler.sendMessage(message);
            }
        }).start();

        //Looper轮询器 取出消息不断处理
        Looper.loop();


    }
}
