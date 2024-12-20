package com.lee.library.uistate

/**
 * UiState ui状态类 支持 flow/liveData 扩展使用
 * @author jv.lee
 * @date 2021/9/18
 */
sealed class UiState {
    data class Success<T>(val data: T) : UiState()
    data class Failure<T>(val data: T?, val exception: Throwable) : UiState()
    data class Error(val exception: Throwable) : UiState()
    object Loading : UiState()
    object Default : UiState()
}

inline fun <reified T> UiState.call(
    crossinline success: (T) -> Unit,
    crossinline error: (Throwable) -> Unit,
    crossinline loading: () -> Unit = {},
    crossinline default: () -> Unit = {}
) {
    when (this) {
        is UiState.Success<*> -> success(this.data as T)
        is UiState.Error -> error(this.exception)
        is UiState.Loading -> loading()
        is UiState.Failure<*> -> {
            (this.data as T?)?.run(success)
            error(this.exception)
        }
        else -> {
            default()
        }
    }
}