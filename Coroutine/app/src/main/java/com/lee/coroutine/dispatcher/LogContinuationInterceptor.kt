package com.lee.coroutine.dispatcher

import com.lee.coroutine.log
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor

/**
 * @author jv.lee
 * @date 2020/3/19
 * @description
 */
class LogContinuationInterceptor : ContinuationInterceptor {
    override val key = ContinuationInterceptor
    override fun <T> interceptContinuation(continuation: Continuation<T>) =
        LogContinuation(continuation)
}

class LogContinuation<T>(val continuation: Continuation<T>) : Continuation<T> {
    override val context = continuation.context
    override fun resumeWith(result: Result<T>) {
        log("<LogContinuation> $result")
        continuation.resumeWith(result)
    }
}