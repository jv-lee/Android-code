package com.lee.review;

public class ReviewTest {

    public static void main(String[] args) {
        final SynchronizedCode sync = new SynchronizedCode();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                sync.fun1();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                sync.fun2();
            }
        });

        thread1.start();
        thread2.start();
    }

}
