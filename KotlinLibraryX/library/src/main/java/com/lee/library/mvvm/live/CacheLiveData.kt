package com.lee.library.mvvm.live

import com.lee.library.mvvm.base.BaseLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author jv.lee
 * @date 2020/5/20
 * @description
 */
class CacheLiveData<T> : BaseLiveData<T>() {

    private var firstCache = true

    fun cacheLaunch(
        startBlock: suspend CoroutineScope.() -> T? = { null },
        resumeBlock: suspend CoroutineScope.() -> T? = { null },
        completedBlock: suspend CoroutineScope.(T) -> Unit = {}
    ) {
        launchMain {
            //首次加载缓存数据
            if (firstCache) {
                firstCache = false
                value = withContext(Dispatchers.IO) { startBlock() }
            }

            //网络数据设置
            val response = withContext(Dispatchers.IO) {
                resumeBlock()
            }

            //缓存数据与本地数据不一致 设置网络数据
            if (value != response) {
                value = response
            }

            //存储缓存数据
            response?.let {
                withContext(Dispatchers.IO) { completedBlock(it) }
            }
        }
    }

}