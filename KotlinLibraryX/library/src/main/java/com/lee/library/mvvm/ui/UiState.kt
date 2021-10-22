package com.lee.library.mvvm.ui

/**
 * @author jv.lee
 * @data 2021/9/18
 * @description
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
    crossinline default: () -> Unit = {},
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