package com.lee.library.mvvm.base

import com.lee.library.utils.LogUtil
import retrofit2.Response

/**
 * @author jv.lee
 * @date 2019/9/26.
 * @description
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

}