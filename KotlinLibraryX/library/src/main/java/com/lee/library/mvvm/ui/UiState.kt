package com.lee.library.mvvm.ui

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*

/**
 * @author jv.lee
 * @data 2021/9/18
 * @description
 */

sealed class UiState {
    data class Success<T>(val data: T) : UiState()
    data class Error(val exception: Throwable) : UiState()
    object Loading : UiState()
    object Default : UiState()
}

inline fun <reified T> stateLiveData(crossinline block: suspend () -> T) = liveData {
    try {
        emit(UiState.Loading)
        emit(UiState.Success(block()))
    } catch (e: Exception) {
        emit(UiState.Error(e))
    }
}

suspend inline fun <reified T> Flow<UiState>.collect(
    crossinline success: (T) -> Unit,
    crossinline error: (Throwable) -> Unit,
    crossinline loading: () -> Unit = {},
    crossinline default: () -> Unit = {},
) {
    collect {
        it.call(success, error, loading, default)
    }
}

inline fun <reified T> LiveData<UiState>.observe(
    owner: LifecycleOwner,
    crossinline success: (T) -> Unit,
    crossinline error: (Throwable) -> Unit,
    crossinline loading: () -> Unit = {},
    crossinline default: () -> Unit = {},
) {
    observe(owner, Observer {
        it.call(success, error, loading, default)
    })
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
        else -> {
            default()
        }
    }
}

fun <T> Flow<T>.uiState(): Flow<UiState> {
    return transform { value ->
        return@transform emit(UiState.Success(value) as UiState)
    }.catch {
        emit(UiState.Error(it))
    }
}