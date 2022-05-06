package com.lee.library.viewstate

import kotlinx.coroutines.flow.*

/**
 * @author jv.lee
 * @date 2021/10/22
 * @description UiState Flow扩展
 */

typealias UiStateFlow = Flow<UiState>
typealias UiStateStateFlow = StateFlow<UiState>
typealias UiStateMutableStateFlow = MutableStateFlow<UiState>

/**
 * uiFlow根据数据源更新当前flow数据值
 * @param requestBlock 数据获取函数
 */
suspend inline fun <T> MutableStateFlow<UiState>.updateState(
    crossinline requestBlock: suspend () -> T
) {
    try {
        update { UiState.Loading }
        update { UiState.Success(requestBlock()) }
    } catch (e: Exception) {
        e.printStackTrace()
        update { UiState.Error(e) }
    }
}

/**
 * uiFlow根据数据源更新当前flow数据值 - 支持缓存数据
 * @param requestBlock 数据获取函数
 * @param cacheBlock 缓存数据获取函数
 * @param completedBlock 数据获取结果回调函数
 */
suspend fun <T> MutableStateFlow<UiState>.updateStateCache(
    requestBlock: suspend () -> T? = { null },
    cacheBlock: suspend () -> T? = { null },
    completedBlock: suspend (T) -> Unit = {}
) {
    var data: T? = null
    try {
        update { UiState.Loading }

        // 加载缓存数据
        data = cacheBlock()?.also { cache ->
            update { UiState.Success(cache) }
        }

        requestBlock()?.also { response ->
            if (data != response) {
                // 更新网络数据
                update { UiState.Success(response) }

                // 数据加载结束回调
                completedBlock(response)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        update { UiState.Failure(data, e) }
    }
}

/**
 * T 数据源转换为uiFlow
 * @param block 数据请求函数
 */
inline fun <reified T> flowState(crossinline block: suspend () -> T) = flow {
    var data: T? = null
    try {
        emit(UiState.Loading)

        data = block()
        emit(UiState.Success(data))
    } catch (e: Exception) {
        emit(UiState.Failure(data, e))
    }
}

/**
 * T 数据源转换为uiFlow数据 - 支持缓存数据
 * @param requestBlock 数据获取函数
 * @param cacheBlock 缓存数据获取函数
 * @param completedBlock 数据获取结果回调函数
 */
inline fun <reified T> flowStateCache(
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

/**
 * flow转uiState flow数据
 */
inline fun <reified T> Flow<T>.uiState(): Flow<UiState> {
    var data: T? = null
    return transform { value ->
        data = value
        emit(UiState.Success(value) as UiState)
    }.onStart {
        emit(UiState.Loading)
    }.catch { e ->
        emit(UiState.Failure(data, e))
    }
}

/**
 * uiStateFlow数据collect扩展
 */
suspend inline fun <reified T> Flow<UiState>.collectState(
    crossinline success: (T) -> Unit,
    crossinline error: (Throwable) -> Unit,
    crossinline loading: () -> Unit = {},
    crossinline default: () -> Unit = {},
) {
    collect {
        try {
            it.call(success, error, loading, default)
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }
}