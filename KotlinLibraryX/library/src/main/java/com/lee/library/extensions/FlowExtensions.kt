package com.lee.library.extensions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn

/**
 *
 * @author jv.lee
 * @date 2020/11/26
 *
 */

fun <T> Flow<T>.notNull(): Flow<T> {
    return this.filter { it != null }
}

fun <T> Flow<T>.dispatchersIO(): Flow<T> {
    return this.flowOn(Dispatchers.IO)
}