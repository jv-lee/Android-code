package com.lee.library.mvvm.ui

import kotlin.random.Random

/**
 * @author jv.lee
 * @date 2022/2/14
 * @description PageUiState 类型 StateFlow分页处理
 * @param requestFirstPage 分页初始请求页码
 * @param responseFirstPage 分页数据返回首页页码
 */
sealed class PageUiState(
    var requestFirstPage: Int = 1,
    var responseFirstPage: Int = 1,
) {
    var page = requestFirstPage
    var firstCache = true

    class Loading constructor(
        requestFirstPage: Int = 1,
        responseFirstPage: Int = 1,
    ) : PageUiState(requestFirstPage, responseFirstPage)

    data class Success<T>(
        val data: T,
        var version: Int = Random.nextInt()
    ) : PageUiState()

    data class Failure<T>(
        val data: T?,
        val exception: Throwable,
        var version: Int = Random.nextInt()
    ) : PageUiState()

    data class Error(
        val exception: Throwable
    ) : PageUiState()

    fun copy(data: PageUiState): PageUiState {
        data.requestFirstPage = requestFirstPage
        data.responseFirstPage = responseFirstPage
        data.page = page
        data.firstCache = firstCache
        return data
    }
}

inline fun <reified T> PageUiState.call(
    crossinline success: (T) -> Unit,
    crossinline error: (Throwable) -> Unit,
    crossinline loading: () -> Unit = {},
) {
    when (this) {
        is PageUiState.Success<*> -> success(this.data as T)
        is PageUiState.Error -> error(this.exception)
        is PageUiState.Failure<*> -> {
            (this.data as T?)?.run(success)
            error(this.exception)
        }
        else -> {
            loading()
        }
    }
}