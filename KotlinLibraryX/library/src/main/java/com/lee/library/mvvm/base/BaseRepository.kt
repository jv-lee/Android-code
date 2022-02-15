package com.lee.library.mvvm.base

import com.lee.library.net.HttpManager
import com.lee.library.net.request.IRequest
import com.lee.library.net.request.Request
import com.lee.library.utils.LogUtil
import retrofit2.Response

/**
 * @author jv.lee
 * @date 2019/9/26.
 * @description repository基类 提供retrofit请求结果响应解析
 */
open class BaseRepository {

    suspend fun <T : Any> apiResponse(call: suspend () -> Response<T>): T? {
        return try {
            call.invoke().body()
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtil.e("network request error :${e.message}")
            null
        }
    }

    inline fun <reified T> createApi(
        baseUri: String,
        request: Request = Request(
            baseUri,
            IRequest.ConverterType.JSON,
            callTypes = intArrayOf(IRequest.CallType.COROUTINE, IRequest.CallType.FLOW)
        )
    ): T {
        return HttpManager.getInstance().getService(T::class.java, request)
    }
}

