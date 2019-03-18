package com.lee.library.handler;

import java.util.UUID;

public class MyClass {

    public static void main(String[] args) {
        //轮循器初始化
        Looper.prepare();

        final Handler handler = new Handler(){
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

                    handler.sendMessage(msg);
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
