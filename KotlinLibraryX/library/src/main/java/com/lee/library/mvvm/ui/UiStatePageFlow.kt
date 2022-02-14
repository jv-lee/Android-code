package com.lee.library.mvvm.ui

import com.lee.library.adapter.page.PagingData
import com.lee.library.mvvm.livedata.LoadStatus
import com.lee.library.utils.LogUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlin.random.Random

/**
 * @author jv.lee
 * @date 2022/2/14
 * @description PageUiState 类型 StateFlow分页处理
 * @param requestFirstPage 分页初始请求页码
 * @param responseFirstPage 分页数据返回首页页码
 */
sealed class PageUiState(
    var requestFirstPage: Int = 1,
    var responseFirstPage: Int = 1,
) {
    var page = requestFirstPage
    var firstCache = true

    object Loading : PageUiState()

    data class Success<T>(
        val data: T,
        var version: Int = Random.nextInt()
    ) : PageUiState()

    data class Failure<T>(
        val data: T?,
        val exception: Throwable,
        var version: Int = Random.nextInt()
    ) : PageUiState()

    data class Error(
        val exception: Throwable
    ) : PageUiState()

    class Default constructor(
        requestFirstPage: Int = 1,
        responseFirstPage: Int = 1,
    ) : PageUiState(requestFirstPage, responseFirstPage)

    fun copy(data: PageUiState): PageUiState {
        data.requestFirstPage = requestFirstPage
        data.responseFirstPage = responseFirstPage
        data.page = page
        data.firstCache = firstCache
        return data
    }
}

/**
 * uiStateFlow数据collect扩展
 */
suspend inline fun <reified T> StateFlow<PageUiState>.stateCollect(
    crossinline success: (T) -> Unit,
    crossinline error: (Throwable) -> Unit,
    crossinline loading: () -> Unit = {},
    crossinline default: () -> Unit = {},
) {
    collect {
        try {
            it.call(success, error, loading, default)
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }
}

inline fun <reified T> PageUiState.call(
    crossinline success: (T) -> Unit,
    crossinline error: (Throwable) -> Unit,
    crossinline loading: () -> Unit = {},
    crossinline default: () -> Unit = {},
) {
    when (this) {
        is PageUiState.Success<*> -> success(this.data as T)
        is PageUiState.Error -> error(this.exception)
        is PageUiState.Loading -> loading()
        is PageUiState.Failure<*> -> {
            (this.data as T?)?.run(success)
            error(this.exception)
        }
        else -> {
            default()
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> MutableStateFlow<PageUiState>.getValueData(): T? {
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

//新旧数据根据页码合并
@Suppress("UNCHECKED_CAST")
fun MutableStateFlow<PageUiState>.applyData(oldItem: PagingData<*>?, newItem: PagingData<*>) {
    oldItem ?: return

    if (oldItem.getDataSource() == newItem.getDataSource()) return

    if (newItem.getPageNumber() != value.responseFirstPage) {
        newItem.getDataSource().addAll(0, oldItem.getDataSource() as Collection<Nothing>)
    }
}

//新旧数据根据页码合并 扩展作用域,直接接收请求数据合并返回请求数据
suspend fun <T : PagingData<*>> MutableStateFlow<PageUiState>.applyData(dataResponse: suspend () -> T): T {
    return dataResponse().also { newData ->
        applyData(getValueData<T>(), newData)
    }
}

/**
 * flow分页数据加载
 */
suspend fun <T> MutableStateFlow<PageUiState>.pageLaunch(
    @LoadStatus status: Int,
    requestBlock: suspend MutableStateFlow<PageUiState>.(Int) -> T? = { null },
    cacheBlock: suspend MutableStateFlow<PageUiState>.() -> T? = { null },
    cacheSaveBlock: suspend MutableStateFlow<PageUiState>.(T) -> Unit = {}
) {
    var response: T? = null
    try {
        //根据加载状态设置页码
        if (status == LoadStatus.REFRESH) {
            value.page = value.requestFirstPage
            //加载更多状态 增加页码
        } else if (status == LoadStatus.LOAD_MORE) {
            value.page++
        }

        //首次加载缓存数据
        if (value.firstCache) {
            value.firstCache = false
            response = cacheBlock()?.also { data ->
                update { value.copy(PageUiState.Success(data = data)) }
            }
        }

        //网络数据设置
        response = requestBlock(value.page)?.also { data ->
            if (response != data) {
                update { value.copy(PageUiState.Success(data = data)) }

                //首页将网络数据设置缓存
                if (value.page == value.requestFirstPage) {
                    cacheSaveBlock(data)
                }
            }
        }
    } catch (e: Exception) {
        LogUtil.getStackTraceString(e)

        response?.let { data ->
            update { value.copy(PageUiState.Failure(data, e)) }
        } ?: kotlin.run {
            update { value.copy(PageUiState.Failure(getValueData<T>(), e)) }
        }
    }
}