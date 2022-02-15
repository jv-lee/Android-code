package com.lee.library.mvvm.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.lee.library.adapter.page.PagingData
import com.lee.library.mvvm.livedata.LoadStatus
import com.lee.library.utils.LogUtil

/**
 * @author jv.lee
 * @date 2022/2/15
 * @description UiStatePage LiveData扩展
 */

// PageUiStateLiveData数据observe扩展
inline fun <reified T> LiveData<PageUiState>.stateObserve(
    owner: LifecycleOwner,
    crossinline success: (T) -> Unit,
    crossinline error: (Throwable) -> Unit,
    crossinline loading: () -> Unit = {},
) {
    observe(owner, Observer {
        try {
            it.call(success, error, loading)
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    })
}

@Suppress("UNCHECKED_CAST")
fun <T> LiveData<PageUiState>.getValueData(): T? {
    val value = this.value
    value ?: return null
    return when (value) {
        is PageUiState.Success<*> -> value.data as? T
        is PageUiState.Failure<*> -> {
            value.data as? T
        }
        else -> {
            null
        }
    }
}

// 新旧数据根据页码合并
@Suppress("UNCHECKED_CAST")
fun LiveData<PageUiState>.applyData(oldItem: PagingData<*>?, newItem: PagingData<*>) {
    oldItem ?: return

    if (oldItem.getDataSource() == newItem.getDataSource()) return

    if (newItem.getPageNumber() != value?.responseFirstPage) {
        newItem.getDataSource().addAll(0, oldItem.getDataSource() as Collection<Nothing>)
    }
}

// 新旧数据根据页码合并 扩展作用域,直接接收请求数据合并返回请求数据
suspend fun <T : PagingData<*>> LiveData<PageUiState>.applyData(dataResponse: suspend () -> T): T {
    return dataResponse().also { newData ->
        applyData(getValueData<T>(), newData)
    }
}

// liveData分页数据加载
suspend fun <T> MutableLiveData<PageUiState>.pageLaunch(
    @LoadStatus status: Int,
    requestBlock: suspend LiveData<PageUiState>.(Int) -> T? = { null },
    cacheBlock: suspend LiveData<PageUiState>.() -> T? = { null },
    cacheSaveBlock: suspend LiveData<PageUiState>.(T) -> Unit = {}
) {
    var response: T? = null
    value?.apply {
        try {
            //根据加载状态设置页码
            if (status == LoadStatus.REFRESH) {
                page = requestFirstPage
                //加载更多状态 增加页码
            } else if (status == LoadStatus.LOAD_MORE) {
                page++
            }

            //首次加载缓存数据
            if (firstCache) {
                firstCache = false
                response = cacheBlock()?.also { data ->
                    postValue(copy(PageUiState.Success(data = data)))
                }
            }

            //网络数据设置
            response = requestBlock(page)?.also { data ->
                if (response != data) {
                    postValue(copy(PageUiState.Success(data = data)))

                    //首页将网络数据设置缓存
                    if (page == requestFirstPage) {
                        cacheSaveBlock(data)
                    }
                }
            }
        } catch (e: Exception) {
            LogUtil.getStackTraceString(e)

            response?.let { data ->
                postValue(copy(PageUiState.Failure(data, e)))
            } ?: kotlin.run {
                postValue(copy(PageUiState.Failure(getValueData<T>(), e)))
            }
        }
    }

}