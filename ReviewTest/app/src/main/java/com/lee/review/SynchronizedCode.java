package com.lee.review;

public class SynchronizedCode {
    private int count = 0;

    public synchronized void fun1() {
        for (int i = 0; i < 10; i++) {
            ++count;
            System.out.println("fun1 - "+count);
        }
    }

    public synchronized void fun2() {
        for (int i = 0; i < 10; i++) {
            ++count;
            System.out.println("fun2 - "+count);
        }
    }

    public int getCount() {
        return count;
    }
}
