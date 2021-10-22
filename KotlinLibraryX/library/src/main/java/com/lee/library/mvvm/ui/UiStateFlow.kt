package com.lee.library.mvvm.ui

import kotlinx.coroutines.flow.*

/**
 * @author jv.lee
 * @data 2021/10/22
 * @description UiState Flow扩展
 */
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
    crossinline requestBlock: suspend () -> T? = { null },
    crossinline cacheBlock: suspend () -> T? = { null },
    crossinline completedBlock: suspend (T) -> Unit = {}
) = flow {
    var data: T? = null
    try {
        emit(UiState.Loading)

        //加载缓存数据
        data = cacheBlock()?.also {
            emit(UiState.Success(it))
        }

        //网络数据
        requestBlock()?.also {
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

inline fun <reified T> uiState(flow: Flow<T>) = flow {
    emit(UiState.Loading)
    emitAll(flow.map { UiState.Success(it) })
}.catch {
    emit(UiState.Error(it))
}

inline fun <reified T> Flow<T>.uiStateTransform() = transform { value ->
    emit(UiState.Loading)
    emit(UiState.Success(value))
}.catch {
    emit(UiState.Error(it))
}

inline fun <reified T> Flow<T>.uiStateFlat() = flatMapConcat {
    flow {
        emit(UiState.Loading)
        emit(UiState.Success(it))
    }
}.catch { e ->
    emit(UiState.Error(e))
}