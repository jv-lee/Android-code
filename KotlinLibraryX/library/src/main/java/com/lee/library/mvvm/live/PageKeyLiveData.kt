package com.lee.library.mvvm.live

import com.lee.library.mvvm.base.BaseLiveData
import kotlinx.coroutines.CoroutineScope

/**
 * @author jv.lee
 * @date 2020/5/20
 * @description
 */
class PageKeyLiveData<T, K>(val initKey: K? = null) : BaseLiveData<T>() {

    private var key: K? = null
    private var nextKey: K? = null
    private var firstCache = true

    fun pageLaunch(
        isRefresh: Boolean = false,
        isLoadMore: Boolean = false,
        isReLoad: Boolean = false,
        startBlock: suspend CoroutineScope.() -> T? = { null },
        resumeBlock: suspend CoroutineScope.(K?) -> T? = { key: K? -> null },
        completedBlock: suspend CoroutineScope.(T) -> Unit = {}
    ) {
        launchMain {
            var response: T? = null

            //根据加载状态设置页码
            //刷新状态 重置页码
            if (isRefresh) {
                key = initKey
                //加载更多状态 增加页码
            } else if (isLoadMore) {
                key = nextKey
                //非重试状态 value不为空则为view重构 直接使用原数据
            } else if (!isReLoad && value != null) {
                return@launchMain
            }

            //首次加载缓存数据
            if (firstCache) {
                firstCache = false
                response = startBlock()?.also {
                    value = it
                }
            }

            //网络数据设置
            response = resumeBlock(key).also {
                if (response != it) {
                    value = it
                }
            }

            //首页将网络数据设置缓存
            if (key == initKey) {
                response?.run {
                    completedBlock(this)
                }
            }
        }
    }

    fun putNextKey(nextKey: K) {
        this.nextKey = nextKey
    }

}