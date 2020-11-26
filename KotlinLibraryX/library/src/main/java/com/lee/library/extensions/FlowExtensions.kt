package com.lee.library.extensions

import com.lee.library.mvvm.base.BaseLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

/**
 * @author jv.lee
 * @date 2020/11/26
 * @description
 */
suspend fun <T> Flow<T>.bindLive(liveData: BaseLiveData<T>) {
    catch {
        liveData.throwMessage(it)
    }.collect {
        liveData.value = it
    }
}