package com.lee.library.mvvm.vm

import android.app.Application
import kotlinx.coroutines.CoroutineScope

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description 设置分页列表ViewModel
 */
open class ResponsePageViewModel(application: Application, private val limit: Int = 0) :
    ResponseViewModel(application) {

    var page = limit
    private var firstCache = true

    /**
     * 分页数据加载封装
     */
    fun <T> pageLaunch(
        isLoadMore: Boolean,
        isReload: Boolean = false,
        startBlock: suspend CoroutineScope.() -> Unit = {},
        resumeBlock: suspend CoroutineScope.() -> T? = { null },
        completedBlock: suspend CoroutineScope.(T) -> Unit = {}
    ) {
        launch(-1) {
            //加载更多设置page
            if (isLoadMore) {
                if (!isReload) page++
            } else {
                page = limit
            }
            //设置首次缓存flag
            if (firstCache) {
                firstCache = false
                startBlock()
            }
            //根据页码回调网络请求及是否调用 completedBlock函数体 (completedBlock处理只使用一次的缓存存储,及数据设置)
            if (page == limit) {
                val response = resumeBlock()
                response?.let {
                    completedBlock(response)
                }
            } else {
                resumeBlock()
            }
        }
    }

    fun <T> cacheLaunch(
        startBlock: suspend CoroutineScope.() -> Unit = {},
        resumeBlock: suspend CoroutineScope.() -> T? = { null },
        completedBlock: suspend CoroutineScope.(T) -> Unit = {}
    ) {
        pageLaunch(
            isLoadMore = false,
            isReload = false,
            startBlock = startBlock,
            resumeBlock = resumeBlock,
            completedBlock = completedBlock
        )
    }

}

