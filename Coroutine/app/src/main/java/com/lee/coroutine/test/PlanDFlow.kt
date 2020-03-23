package com.lee.coroutine.test

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * @author jv.lee
 * @date 2020/3/19
 * @description
 */
class PlanDFlow {

    fun test() {
        GlobalScope.launch(Dispatchers.Main) {
            //            testFlow()
//            testMap()
//            testMapOrFirst()
//            testChannelFlow()
        }
    }

    suspend fun testFlow() {
        println("test.flow 自动发送 emit")
        listOf(1, 2, 3, 4).asFlow()
            .catch {
                println("catch:${it.message}")
            }
            .onCompletion {
                println("onCompletion")
            }
            .collect() {
                println("collect:$it")
            }
    }

    suspend fun testMap() {
        //数据源线程
        listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
            .asFlow()
            .map { i ->
                println("threadName:${Thread.currentThread().name}")
                i + 1
            }
            .filter { i ->
                println("threadName:${Thread.currentThread().name}")
                i < 5
            }
            .flowOn(Dispatchers.IO)
            .collect {
                // 1(2) 2(3) 3(4) 0(1)
                println("threadName:${Thread.currentThread().name}")
                println("value:$it")
            }

        flowOf(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0))
            .map {
                it.filter { it < 5 }
            }
            .collect {
                println("value:$it")
            }

        //发射数据 组合每一项数据
        listOf(1, 2, 3)
            .asFlow()
            .zip(listOf("one", "two", "three").asFlow()) { a, b ->
                "$a -> $b"
            }.collect {
                println(it)
            }

        //合并两组数据源
        flowOf(arrayListOf(1, 2, 3))
            .zip(flowOf(arrayListOf(4, 5, 6))) { list, newList ->
                list.addAll(newList)
                list
            }
            .collect {
                println("listSize:${it.size}")
            }

    }

    suspend fun testMapOrFirst() {
        //发射三组数据
        flowOf(memory(), disk(), network())
            .map {
                it.await()
            }
            .flowOn(Dispatchers.IO)
            .collect {
                println("获取数据:$it")
            }

        //满足单一条件才返回
        val first = flowOf(memory(), disk(), network())
            .map {
                it.await()
            }
            .flowOn(Dispatchers.IO)
            .first {
                it == "Disk Data"
            }
        println("value:$first")
    }

    fun memory(): Deferred<String> {
        return CompletableDeferred("Memory Data")
    }

    fun disk(): Deferred<String> {
        return CompletableDeferred("Disk Data")
    }

    fun network(): Deferred<String> {
        return CompletableDeferred("Network Data")
    }

    /**
     * channel 内支持调度器 withContext
     */
    suspend fun testChannelFlow() {
        channelFlow<String> {
            println("threadName:${Thread.currentThread().name}")
            send("this is send value")
            withContext(Dispatchers.IO) {
                println("threadName:${Thread.currentThread().name}")
                send("this is childThread send value")
            }
        }.collect {
            println(it)
        }
    }

    /**
     * select 选择
     */
    suspend fun testConcat() {

    }

}