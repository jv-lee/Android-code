package com.lee.library.mvvm.live

import androidx.annotation.IntDef
import com.lee.library.mvvm.base.BaseLiveData
import com.lee.library.mvvm.live.LoadStatus.Companion.LOAD_MORE
import com.lee.library.mvvm.live.LoadStatus.Companion.REFRESH
import com.lee.library.mvvm.live.LoadStatus.Companion.RELOAD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author jv.lee
 * @date 2020/5/20
 * @description
 */

@IntDef(REFRESH, LOAD_MORE, RELOAD)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class LoadStatus {

    companion object {
        const val REFRESH: Int = 0x001
        const val LOAD_MORE: Int = 0x002
        const val RELOAD: Int = 0x003
    }
}

class PageLiveData<T>(val limit: Int = 0) : BaseLiveData<T>() {

    var page = limit
    private var firstCache = true

    fun pageLaunch(
        @LoadStatus status: Int,
        networkBlock: suspend CoroutineScope.(Int) -> T? = { page: Int -> null },
        cacheBlock: suspend CoroutineScope.() -> T? = { null },
        cacheSaveBlock: suspend CoroutineScope.(T) -> Unit = {}
    ) {
        launchMain {
            var response: T? = null

            //根据加载状态设置页码
            //刷新状态 重置页码
            if (status == REFRESH) {
                page = limit
                //加载更多状态 增加页码
            } else if (status == LOAD_MORE) {
                page++
                //非重试状态 value不为空则为view重构 直接使用原数据
            } else if (status != RELOAD && value != null) {
                return@launchMain
            }

            //首次加载缓存数据
            if (firstCache) {
                firstCache = false
                response = withContext(Dispatchers.IO) { cacheBlock() }
                response?.let { value = it }
            }

            //网络数据设置
            response = withContext(Dispatchers.IO) {
                networkBlock(page).also {
                    withContext(Dispatchers.Main) {
                        if (response != it) {
                            value = it
                        }
                    }
                }
            }

            //首页将网络数据设置缓存
            if (page == limit) {
                response?.let {
                    withContext(Dispatchers.IO) {
                        cacheSaveBlock(it)
                    }
                }
            }
        }
    }

}