package com.lee.library.mvvm.ui

import androidx.lifecycle.LiveData
import com.lee.library.mvvm.livedata.LoadStatus
import com.lee.library.utils.LogUtil

/**
 * @author jv.lee
 * @data 2021/10/21
 * @description
 */
class UiStatePageLiveData(internal val initPage: Int = 0) : LiveData<UiState>() {
    private var page = initPage
    private var firstCache = true

    fun <T> getValueData(): T? {
        val value = this.value
        value ?: return null
        return when (value) {
            is UiState.Success<*> -> value.data as T
            is UiState.Failure<*> -> {
                value.data as T?
            }
            else -> {
                null
            }
        }
    }

    suspend fun <T> pageLaunch(
        @LoadStatus status: Int,
        networkBlock: suspend (Int) -> T? = { null },
        cacheBlock: suspend () -> T? = { null },
        cacheSaveBlock: suspend (T) -> Unit = {}
    ) {
        var response: T? = null
        try {
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
                    postValue(UiState.Success(it))
                }
            }

            //网络数据设置
            response = networkBlock(page).also {
                if (response != it) {
                    postValue(UiState.Success(it))
                }
            }

            //首页将网络数据设置缓存
            if (page == initPage) {
                response?.run {
                    cacheSaveBlock(this)
                }
            }
        } catch (e: Exception) {
            LogUtil.getStackTraceString(e)

            response?.let {
                postValue(UiState.Failure(it, e))
            } ?: kotlin.run {
                postValue(UiState.Failure(getValueData<T>(), e))
            }
        }
    }

    //分页数据合并
    fun <T> applyData(
        oldData: ArrayList<T>?,
        newData: ArrayList<T>
    ) {
        oldData ?: return
        if (page != initPage) newData.addAll(0, oldData)
    }

}