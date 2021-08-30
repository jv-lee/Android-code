package com.lee.coroutine.test

import android.util.Log
import kotlinx.coroutines.*

/**
 * @author jv.lee
 * @data 2021/8/30
 * @description
 */
class PlanEModel {

    private val TAG = "PlanEModel"

    /**
     * suspend方法使用携程作用域
     */
    suspend fun suspendFun() = coroutineScope {
        //同步代码
        Log.i(TAG, "1.同步代码：${Thread.currentThread().name}")
        launch(Dispatchers.IO) {
            //异步代码
            Log.i(TAG, "3.异步代码：${Thread.currentThread().name}")
        }
        //同步代码
        Log.i(TAG, "2.同步代码：${Thread.currentThread().name}")
    }

    /**
     * 普通方法使用携程作用域
     */
    fun simpleFun() = runBlocking {
        launch {
            Log.i(TAG, "1.${Thread.currentThread().name}")
            withContext(Dispatchers.IO) {
                Log.i(TAG, "2.${Thread.currentThread().name}")
            }
            Log.i(TAG, "3.${Thread.currentThread().name}")
        }
    }

    /**
     * 并行执行任务
     */
    fun simple2Fun() = runBlocking {
        launch {
            val startTime = System.currentTimeMillis()

            val task1 = async(Dispatchers.IO) {
                delay(2000)
                Log.i(TAG, "1.执行task1...${Thread.currentThread().name}")
                "one"
            }

            val task2 = async(Dispatchers.IO) {
                delay(1000)
                Log.i(TAG, "2.执行task2...${Thread.currentThread().name}")
                "two"
            }

            Log.i(
                TAG,
                "3. task1=${task1.await()} , task2=${task2.await()} , 耗时:${System.currentTimeMillis() - startTime}"
            )
        }
    }

    /**
     * 同步进行任务
     */
    fun simple3Fun() = runBlocking {
        launch {
            val startTime = System.currentTimeMillis()

            val task1 = withContext(Dispatchers.IO) {
                delay(2000)
                Log.i(TAG, "1.执行task1...${Thread.currentThread().name}")
                "one"
            }

            val task2 = withContext(Dispatchers.IO) {
                delay(1000)
                Log.i(TAG, "2.执行task2...${Thread.currentThread().name}")
                "two"
            }

            Log.i(
                TAG,
                "3. task1=${task1} , task2=${task2} , 耗时:${System.currentTimeMillis() - startTime}"
            )
        }
    }

}