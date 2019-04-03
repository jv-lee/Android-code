package com.lee.library.handler;

import java.util.UUID;

public class MyClass {
    static Handler H;

    public static void main(String[] args) {
        //轮循器初始化 好比ActivityThread中 Looper.prepareMainLooper 得到一个主线程调用的轮询起 Looper
        Looper.prepare();

        //实列化自身后 获取成员变量 H

        H = new Handler(){
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                System.out.println("handler = " + Thread.currentThread().getName() + message.obj);
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(){
                @Override
                public void run() {
                    Message msg = new Message();
                    synchronized (UUID.class) {
                        msg.obj = UUID.randomUUID().toString();
                    }
                    System.out.println("send = "+Thread.currentThread().getName() + " = "+msg.obj);

                    H.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }

        //轮询操作
        Looper.loop();
    }
}
