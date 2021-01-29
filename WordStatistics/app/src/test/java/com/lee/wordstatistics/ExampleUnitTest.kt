package com.lee.wordstatistics

import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.Executors

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        //按顺序队列执行的线程池
        val executors = Executors.newSingleThreadExecutor()
        executors.submit {
            println(1)
        }
        executors.submit {
            println(2)
        }
        executors.submit {
            println(3)
        }
        executors.submit {
            println(4)
        }
    }
}