package com.lee.library.mvvm.livedata

import com.lee.library.extensions.dispatchersIO
import com.lee.library.extensions.notNull
import com.lee.library.mvvm.base.BaseLiveData
import com.lee.library.mvvm.load.LoadStatus
import com.lee.library.mvvm.load.LoadStatus.Companion.INIT
import com.lee.library.mvvm.load.LoadStatus.Companion.LOAD_MORE
import com.lee.library.mvvm.load.LoadStatus.Companion.REFRESH
import com.lee.library.mvvm.load.LoadStatus.Companion.RELOAD
import kotlinx.coroutines.flow.*
import java.lang.Exception

/**
 * @author jv.lee
 * @date 2020/5/20
 * @description
 */

class PageLiveData<T>(val limit: Int = 0) : BaseLiveData<T>() {

    var page = limit
    private var firstCache = true

    suspend fun pageLaunch(
        @LoadStatus status: Int,
        networkBlock: suspend (Int) -> T? = { page: Int -> null },
        cacheBlock: suspend () -> T? = { null },
        cacheSaveBlock: suspend (T) -> Unit = {}
    ) {
        try {
            var response: T? = null

            //根据加载状态设置页码
            if (status == INIT) {
                //Activity重启 直接使用原有数据渲染
                value?.let { return }
                //刷新状态 重置页码
            } else if (status == REFRESH) {
                page = limit
                //加载更多状态 增加页码
            } else if (status == LOAD_MORE) {
                page++
                //非重试状态 value不为空则为view重构 直接使用原数据
            } else if (status != RELOAD && value != null) {
                return
            }

            //首次加载缓存数据
            if (firstCache) {
                firstCache = false
                response = cacheBlock()?.also {
                    value = it
                }
            }

            //网络数据设置
            response = networkBlock(page).also {
                if (response != it) {
                    value = it
                }
            }

            //首页将网络数据设置缓存
            if (page == limit) {
                response?.run {
                    cacheSaveBlock(this)
                }
            }
        } catch (e: Exception) {
            throwMessage(e)
        }
    }

    suspend fun pageLaunchFlow(
        @LoadStatus status: Int,
        networkBlock: suspend (Int) -> Flow<T?> = { flowOf(null) },
        cacheBlock: suspend () -> Flow<T?> = { flowOf(null) },
        completerBlock: suspend (T) -> T = { it }
    ) {
        //根据加载状态设置页码
        if (status == INIT) {
            //Activity重启 直接使用原有数据渲染
            value?.let { return }
            //刷新状态 重置页码
        } else if (status == REFRESH) {
            page = limit
            //加载更多状态 增加页码
        } else if (status == LOAD_MORE) {
            page++
            //非重试状态 value不为空则为view重构 直接使用原数据
        } else if (status != RELOAD && value != null) {
            return
        }

        flow {
            //首次加载缓存数据
            if (firstCache) {
                firstCache = false
                emitAll(cacheBlock())
            }
            //加载网络数据
            emitAll(networkBlock(page))
        }
            .notNull()
            .map {
                //该回调中saveCache/applyData
                completerBlock(it!!)
            }
            .dispatchersIO()
            .catch {
                throwMessage(it)
            }
            .collect {
                //设置数据
                value = it
            }

    }

}

//分页数据合并
fun <T> PageLiveData<*>.applyData(
    oldData: ArrayList<T>?,
    newData: ArrayList<T>
) {
    oldData ?: return
    if (page != limit) newData.addAll(0, oldData)
}