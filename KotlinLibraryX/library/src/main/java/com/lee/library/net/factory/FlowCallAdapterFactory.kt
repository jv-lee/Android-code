package com.lee.library.net.factory

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * @author jv.lee
 * @date 2020/11/26
 * @description
 */
class FlowCallAdapterFactory private constructor() : CallAdapter.Factory() {
    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke() = FlowCallAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Flow::class.java != getRawType(returnType)) {
            return null
        }
        check(returnType is ParameterizedType) { "Flow return type must be parameterized as Flow<Foo> or Flow<out Foo>" }
        val responseType = getParameterUpperBound(0, returnType)

        val rawFlowType = getRawType(responseType)
        return if (rawFlowType == Response::class.java) {
            check(responseType is ParameterizedType){ "Response must be parameterized as Response<Foo> or Response<out Foo>" }
            ResponseCallAdapter<Any>(getParameterUpperBound(0, responseType))
        } else {
            BodyCallAdapter<Any>(responseType)
        }
    }

    private class BodyCallAdapter<T>(
        private val responseType: Type
    ) : CallAdapter<T, Flow<T>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): Flow<T> {
            return flow {
                emit(
                    suspendCancellableCoroutine<T> { continuation ->
                        call.enqueue(object : Callback<T> {
                            override fun onFailure(call: Call<T>, t: Throwable) {
                                continuation.resumeWithException(t)
                            }

                            override fun onResponse(call: Call<T>, response: Response<T>) {
                                try {
                                    continuation.resume(response.body()!!)
                                } catch (e: Exception) {
                                    continuation.resumeWithException(e)
                                }
                            }
                        })
                        continuation.invokeOnCancellation { call.cancel() }
                    }
                )
            }
        }
    }

    private class ResponseCallAdapter<T>(
        private val responseType: Type
    ) : CallAdapter<T, Flow<Response<T>>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): Flow<Response<T>> {
            return flow {
                emit(
                    suspendCancellableCoroutine<Response<T>> { continuation ->
                        call.enqueue(object : Callback<T> {
                            override fun onFailure(call: Call<T>, t: Throwable) {
                                continuation.resumeWithException(t)
                            }

                            override fun onResponse(call: Call<T>, response: Response<T>) {
                                continuation.resume(response)
                            }

                        })
                        continuation.invokeOnCancellation { call.cancel() }
                    }
                )
            }
        }
    }
}