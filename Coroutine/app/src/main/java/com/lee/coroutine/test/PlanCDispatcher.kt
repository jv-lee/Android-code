package com.lee.coroutine.test

import com.lee.coroutine.dispatcher.LogContinuationInterceptor
import com.lee.coroutine.log
import kotlinx.coroutines.*

/**
 * @author jv.lee
 * @date 2020/3/19
 * @description 3.携程调度器
 */
class PlanCDispatcher {

    fun test() {
        GlobalScope.launch {
            interceptor()
        }
    }

    suspend fun interceptor() {
        GlobalScope.launch(LogContinuationInterceptor()) {
            log(1)
            val job = async {
                log(2)
                delay(1000)
                log(3)
                "Hello"
            }
            log(4)
            val result = job.await()
            log("5. $result")
        }.join()
        log(6)

    }

}