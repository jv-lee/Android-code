package com.lee.library.mvvm.livedata

import com.lee.library.mvvm.load.LoadStatus

/**
 * @author jv.lee
 * @data 2021/9/22
 * @description
 */

class PageLiveData<T>(internal val initPage: Int = 0) : CacheLiveData<T>() {

    internal var page = initPage
}

//分页数据合并
fun <T> PageLiveData<*>.applyData(
    oldData: ArrayList<T>?,
    newData: ArrayList<T>
) {
    oldData ?: return
    if (page != initPage) newData.addAll(0, oldData)
}

suspend fun <T> PageLiveData<*>.pageLaunch(
    @LoadStatus status: Int,
    networkBlock: suspend (Int) -> T? = { _: Int -> null },
    cacheBlock: suspend () -> T? = { null },
    cacheSaveBlock: suspend (T) -> Unit = {}
) {
    try {
        var response: T? = null

        //根据加载状态设置页码
        if (status == LoadStatus.INIT) {
            //Activity重启 直接使用原有数据渲染
            value?.let { return }
            //刷新状态 重置页码
        } else if (status == LoadStatus.REFRESH) {
            page = initPage
            //加载更多状态 增加页码
        } else if (status == LoadStatus.LOAD_MORE) {
            page++
            //非重试状态 value不为空则为view重构 直接使用原数据
        } else if (status != LoadStatus.RELOAD && value != null) {
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
        if (page == initPage) {
            response?.run {
                cacheSaveBlock(this)
            }
        }
    } catch (e: Exception) {
        throwMessage(e)
    }
}

class PageLiveData2<T, K>(internal val initKey: K? = null) : CacheLiveData<T>() {

    internal var key: K? = null
    internal var nextKey: K? = null

    fun putNextKey(nextKey: K) {
        this.nextKey = nextKey
    }
}

suspend fun <T, K> PageLiveData2<T, K>.pageLaunch(
    @LoadStatus status: Int,
    startBlock: suspend () -> T? = { null },
    resumeBlock: suspend (K?) -> T? = { _: K? -> null },
    completedBlock: suspend (T) -> Unit = {}
) {
    try {
        var response: T? = null

        //根据加载状态设置页码
        //刷新状态 重置页码
        if (status == LoadStatus.INIT) {
            //Activity重启 直接使用原有数据渲染
            value?.let { return }
        } else if (status == LoadStatus.REFRESH) {
            key = initKey
            //加载更多状态 增加页码
        } else if (status == LoadStatus.LOAD_MORE) {
            key = nextKey
            //非重试状态 value不为空则为view重构 直接使用原数据
        } else if (status != LoadStatus.RELOAD && value != null) {
            return
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
    } catch (e: Exception) {
        throwMessage(e)
    }
}

