package com.lee.library.mvvm.vm

import android.app.Application
import com.lee.library.mvvm.base.BaseViewModel
import kotlinx.coroutines.coroutineScope

/**
 * @author jv.lee
 * @date 2020/4/21
 * @description 校验返回数据
 */
open class ResponseViewModel(application: Application) :
    BaseViewModel(application) {

    /**
     * 获取网络数据返回非空判断
     */
    suspend fun <T> executeResponseAny(response: T?, successBlock: (T) -> Unit) {
        coroutineScope {
            when (response) {
                null -> {
                    throw Exception("response is null")
                }
                else -> {
                    successBlock(response)
                }
            }
        }
    }

}