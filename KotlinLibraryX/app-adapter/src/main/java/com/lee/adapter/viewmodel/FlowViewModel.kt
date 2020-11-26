package com.lee.adapter.viewmodel

import com.lee.adapter.entity.Content
import com.lee.adapter.entity.Page
import com.lee.adapter.repository.FlowRepository
import com.lee.library.extensions.bindLive
import com.lee.library.extensions.dispatchersIO
import com.lee.library.extensions.notNull
import com.lee.library.mvvm.base.BaseLiveData
import com.lee.library.mvvm.base.BaseViewModel
import com.lee.library.mvvm.load.LoadStatus
import com.lee.library.mvvm.load.PageNumber
import com.lee.library.utils.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 * @author jv.lee
 * @date 2020/11/26
 * @description
 */
class FlowViewModel : BaseViewModel() {

    private val repository by lazy { FlowRepository() }
    private val page by lazy { PageNumber(1) }

    val dataLiveData by lazy { BaseLiveData<Page<Content>>() }

    fun getData(@LoadStatus status: Int) {

        launchMain {
            repository.getData(page.getPage(status))
                .map {
                    val value = withContext(Dispatchers.IO) { 1 }
                    it
                }
                .bindLive(dataLiveData)
        }

    }

    private suspend fun getCache(): String? {
        return null
    }

    private suspend fun getNetwork() = flowOf("network-data")

    private suspend fun putCache() {
        println("put cache data.")
    }

    private fun hasCache() = true

    @ExperimentalCoroutinesApi
    fun getCacheOrNetworkData1() {
        launchMain {
            flow {
                emitAll(flowOf(getCache()))
                emitAll(getNetwork())
            }
                .onStart {
                    LogUtil.i("onStart - threadName:${Thread.currentThread().name}")
                }
                .notNull()
                .dispatchersIO()
                .collect {
                    LogUtil.i("collect - threadName:${Thread.currentThread().name} - data:$it")
                }
        }
    }

    /**
     * 模拟获取缓存数据flow使用
     */
    @FlowPreview
    fun getCacheOrNetworkData2() {
        launchMain {
            flowOf(hasCache())
                .flatMapMerge { hasCache ->
                    //是否使用缓存
                    if (hasCache) {
                        getCache()?.let { return@flatMapMerge flowOf(it) }
                    }
                    return@flatMapMerge getNetwork()
                }.map {
                    putCache()
                    it
                }.flowOn(Dispatchers.IO)
                .catch {

                }.collect {

                }
        }
    }


}