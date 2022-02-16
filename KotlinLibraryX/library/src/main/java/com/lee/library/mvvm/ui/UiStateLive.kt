package com.lee.library.mvvm.ui

import androidx.lifecycle.*

/**
 * @author jv.lee
 * @date 2021/10/22
 * @description UiState LiveData扩展
 */

typealias UiStateLiveData = LiveData<UiState>
typealias UiStateMutableLiveData = MutableLiveData<UiState>

inline fun <reified T> LiveData<UiState>.stateObserve(
    owner: LifecycleOwner,
    crossinline success: (T) -> Unit,
    crossinline error: (Throwable) -> Unit = {},
    crossinline loading: () -> Unit = {},
    crossinline default: () -> Unit = {},
) {
    observe(owner, Observer {
        try {
            it.call(success, error, loading, default)
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    })
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
    crossinline requestBlock: suspend () -> T? = { null },
    crossinline cacheBlock: suspend () -> T? = { null },
    crossinline completedBlock: suspend (T) -> Unit = {}
) = liveData {
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