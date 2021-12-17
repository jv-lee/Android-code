package com.lee.library.net.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author jv.lee
 * @date 2021/11/1
 * @description 重试拦截器
 */
class RetryInterceptor(
    var maxRetry: Int
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        var retryNum = 0
        while (!response.isSuccessful && retryNum < maxRetry) {
            retryNum++
            response = chain.proceed(request)
        }
        return response
    }
}