package com.lee.review

import org.junit.Test
import java.util.concurrent.locks.ReentrantLock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    /**
     * 同步代码块
     */
    @Test
    fun test1() {
        val synchronized = Synchronized()
        val thread1 = Thread {
            synchronized(this) {
                for (index in 0..9) {
                    println("thread1 - ${synchronized.fun1()}")
                }
            }
        }
        val thread2 = Thread {
            synchronized(this) {
                for (index in 0..9) {
                    println("thread2 - ${synchronized.fun1()}")
                }
            }
        }

        thread1.start()
        thread2.start()
    }


    /**
     * 调用同步方法
     */
    @Test
    fun test2() {
        val synchronized = Synchronized()
        val thread1 = Thread {
            for (index in 0..9) {
                println("thread1 - ${synchronized.fun1()}")
            }
        }
        val thread2 = Thread {
            for (index in 0..9) {
                println("thread2 - ${synchronized.fun1()}")
            }
        }

        thread1.start()
        thread2.start()
    }

    private val mLock = ReentrantLock()

    /**
     * 使用ReentrantLock类来同步代码逻辑执行
     * 防止异常造成死锁
     */
    @Test
    fun test3() {
        val synchronized = Synchronized()
        val thread1 = Thread {
            try {
                mLock.lock()
                for (index in 0..9) {
                    println("thread1 - ${synchronized.fun3()}")
                }
            } finally {
                mLock.unlock()
            }
        }
        val thread2 = Thread {
            try {
                mLock.lock()
                for (index in 0..9) {
                    println("thread2 - ${synchronized.fun3()}")
                }
            } finally {
                mLock.unlock()
            }
        }

        thread1.start()
        thread2.start()
    }
}