package com.lee.library.viewstate

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.reflect.KProperty1

/**
 * @author jv.lee
 * @date 2022/4/27
 * @description
 */

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