package com.lee.library.mvvm.livedata

import com.lee.library.mvvm.base.BaseLiveData

/**
 * @author jv.lee
 * @date 2020/5/20
 * @description
 */
open class CacheLiveData<T> : BaseLiveData<T>() {

    internal var firstCache = true
}

suspend fun <T> CacheLiveData<T>.cacheLaunch(
    startBlock: suspend () -> T? = { null },
    resumeBlock: suspend () -> T? = { null },
    completedBlock: suspend (T) -> Unit = {}
) {
    try {
        //首次加载缓存数据
        if (firstCache) {
            firstCache = false
            startBlock()?.run { value = this }
        }

        //网络数据设置
        val response = resumeBlock().also {
            //缓存数据与本地数据不一致 设置网络数据
            if (value != it) {
                value = it
            }
        }

        //存储缓存数据
        response?.run { completedBlock(this) }
    } catch (e: Exception) {
        throwMessage(e)
    }
}