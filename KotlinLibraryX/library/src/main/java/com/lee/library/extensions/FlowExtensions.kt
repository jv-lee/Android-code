/*
 * Flow扩展函数帮助类
 * @author jv.lee
 * @date 2022/8/11
 */
package com.lee.library.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.reflect.KProperty1

/**
 * flow 添加延时回调
 */
fun <T> Flow<T>.delay(timeMillis: Long = 300) = map {
    kotlinx.coroutines.delay(timeMillis)
    it
}

/**
 * flow 添加最小执行时间回调 （如果从执行到collect未满足该时间则进行延时）
 */
fun <T> Flow<T>.lowestTime(timeMillis: Long = 300): Flow<T> {
    var startTime: Long = 0
    return onStart {
        startTime = System.currentTimeMillis()
    }.map {
        val currentTime = System.currentTimeMillis()
        val executionTime = (currentTime - startTime)
        if (executionTime < timeMillis) {
            kotlinx.coroutines.delay(timeMillis - executionTime)
        }
        it
    }
}

suspend fun <T, A> Flow<T>.collectState(
    prop1: KProperty1<T, A>,
    action: (A) -> Unit
) {
    this.map {
        StateTuple1(prop1.get(it))
    }.distinctUntilChanged().collect { (a) ->
        action(a)
    }
}

suspend fun <T, A, B> Flow<T>.collectState(
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    action: (A, B) -> Unit
) {
    this.map {
        StateTuple2(prop1.get(it), prop2.get(it))
    }.distinctUntilChanged().collect { (a, b) ->
        action(a, b)
    }
}

internal data class StateTuple1<A>(val a: A)
internal data class StateTuple2<A, B>(val a: A, val b: B)