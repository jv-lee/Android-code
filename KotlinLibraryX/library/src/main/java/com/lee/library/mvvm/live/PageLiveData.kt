package com.lee.library.mvvm.live

import com.lee.library.mvvm.base.BaseLiveData
import kotlinx.coroutines.CoroutineScope

/**
 * @author jv.lee
 * @date 2020/5/20
 * @description
 */
class PageLiveData<T>(val limit: Int = 0) : BaseLiveData<T>() {

    var page = limit
    private var firstCache = true

    fun pageLaunch(
        isLoadMore: Boolean = false,
        isReload: Boolean = false,
        startBlock: suspend CoroutineScope.() -> T? = { null },
        resumeBlock: suspend CoroutineScope.(Int, Int) -> T? = { _: Int, _: Int -> null },
        completedBlock: suspend CoroutineScope.(T) -> Unit = {}
    ) {
        launch {
            //加载更多设置page
            if (isLoadMore) {
                if (!isReload) page++
            } else {
                page = limit
            }
            //首次加载缓存数据
            if (firstCache) {
                firstCache = false
                startBlock()?.run { value = this }
            }

            //网络数据设置
            val response = resumeBlock(page, limit).also { value = it }

            //首页将网络数据设置缓存
            if (page == limit) {
                response?.run { completedBlock(this) }
            }
        }
    }

}