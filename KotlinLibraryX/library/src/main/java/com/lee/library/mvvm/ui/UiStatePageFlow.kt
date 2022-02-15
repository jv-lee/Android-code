package com.lee.library.mvvm.ui

import com.lee.library.adapter.page.PagingData
import com.lee.library.mvvm.livedata.LoadStatus
import com.lee.library.utils.LogUtil
import kotlinx.coroutines.flow.*

/**
 * @author jv.lee
 * @date 2022/2/15
 * @description UiStatePage Flow扩展
 */

typealias UiStatePageFlow = Flow<UiStatePage>
typealias UiStatePageStateFlow = StateFlow<UiStatePage>
typealias UiStatePageMutableStateFlow = MutableStateFlow<UiStatePage>

val StateFlow<UiStatePage>.page: Int
    get() = value.page
val StateFlow<UiStatePage>.requestFirstPage: Int
    get() = value.requestFirstPage
val StateFlow<UiStatePage>.responseFirstPage: Int
    get() = value.responseFirstPage

// PageUiStateFlow数据collect扩展
suspend inline fun <reified T> StateFlow<UiStatePage>.stateCollect(
    crossinline success: (T) -> Unit,
    crossinline error: (Throwable) -> Unit,
    crossinline default: () -> Unit = {},
) {
    collect {
        try {
            it.call(success, error, default)
        } catch (e: Exception) {
            e.printStackTrace()
            error(e)
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> MutableStateFlow<UiStatePage>.getValueData(): T? {
    val value = this.value
    value ?: return null
    return when (value) {
        is UiStatePage.Success<*> -> value.data as? T
        is UiStatePage.Failure<*> -> {
            value.data as? T
        }
        else -> {
            null
        }
    }
}

// 新旧数据根据页码合并
@Suppress("UNCHECKED_CAST")
fun MutableStateFlow<UiStatePage>.applyData(oldItem: PagingData<*>?, newItem: PagingData<*>) {
    oldItem ?: return

    if (oldItem.getDataSource() == newItem.getDataSource()) return

    if (newItem.getPageNumber() != value.responseFirstPage) {
        newItem.getDataSource().addAll(0, oldItem.getDataSource() as Collection<Nothing>)
    }
}

// 新旧数据根据页码合并 扩展作用域,直接接收请求数据合并返回请求数据
suspend fun <T : PagingData<*>> MutableStateFlow<UiStatePage>.applyData(dataResponse: suspend () -> T): T {
    return dataResponse().also { newData ->
        applyData(getValueData<T>(), newData)
    }
}

// flow分页数据加载
suspend fun <T> MutableStateFlow<UiStatePage>.pageLaunch(
    @LoadStatus status: Int,
    requestBlock: suspend MutableStateFlow<UiStatePage>.(Int) -> T? = { null },
    cacheBlock: suspend MutableStateFlow<UiStatePage>.() -> T? = { null },
    cacheSaveBlock: suspend MutableStateFlow<UiStatePage>.(T) -> Unit = {}
) {
    var response: T? = null
    value.apply {
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
                    update { copy(UiStatePage.Success(data = data)) }
                }
            }

            //网络数据设置
            response = requestBlock(page)?.also { data ->
                if (response != data) {
                    update { copy(UiStatePage.Success(data = data)) }

                    //首页将网络数据设置缓存
                    if (page == requestFirstPage) {
                        cacheSaveBlock(data)
                    }
                }
            }
        } catch (e: Exception) {
            LogUtil.getStackTraceString(e)

            response?.let { data ->
                update { copy(UiStatePage.Failure(data, e)) }
            } ?: kotlin.run {
                update { copy(UiStatePage.Failure(getValueData<T>(), e)) }
            }
        }
    }
}