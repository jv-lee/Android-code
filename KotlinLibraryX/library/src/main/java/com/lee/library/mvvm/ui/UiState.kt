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
    data class Failure<T>(val data: T?, val exception: Throwable) : UiState()
    data class Error(val exception: Throwable) : UiState()
    object Loading : UiState()
    object Default : UiState()
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
        is UiState.Failure<*> -> {
            (this.data as T?)?.run(success)
            error(this.exception)
        }
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

inline fun <reified T> stateLive(crossinline block: suspend () -> T) = liveData {
    var data: T? = null
    try {
        emit(UiState.Loading)

        data = block()
        emit(UiState.Success(data))
    } catch (e: Exception) {
        emit(UiState.Failure(data, e))
    }
}

inline fun <reified T> stateCacheLive(
    crossinline startBlock: suspend () -> T? = { null },
    crossinline resumeBlock: suspend () -> T? = { null },
    crossinline completedBlock: suspend (T) -> Unit = {}
) = liveData {
    var data: T? = null
    try {
        emit(UiState.Loading)

        //加载缓存数据
        data = startBlock()?.also {
            emit(UiState.Success(it))
        }

        //网络数据
        resumeBlock()?.also {
            if (data != it) {
                //发送网络数据
                emit(UiState.Success(it))
                //发送存储本地数据
                completedBlock(it)
            }
        }
    } catch (e: Exception) {
        emit(UiState.Failure(data, e))
    }
}

inline fun <reified T> stateFlow(crossinline block: suspend () -> T) = flow {
    var data: T? = null
    try {
        emit(UiState.Loading)

        data = block()
        emit(UiState.Success(data))
    } catch (e: Exception) {
        emit(UiState.Failure(data, e))
    }
}

inline fun <reified T> stateCacheFlow(
    crossinline startBlock: suspend () -> T? = { null },
    crossinline resumeBlock: suspend () -> T? = { null },
    crossinline completedBlock: suspend (T) -> Unit = {}
) = flow {
    var data: T? = null
    try {
        emit(UiState.Loading)

        //加载缓存数据
        data = startBlock()?.also {
            emit(UiState.Success(it))
        }

        //网络数据
        resumeBlock()?.also {
            if (data != it) {
                //发送网络数据
                emit(UiState.Success(it))
                //发送存储本地数据
                completedBlock(it)
            }
        }
    } catch (e: Exception) {
        emit(UiState.Failure(data, e))
    }
}