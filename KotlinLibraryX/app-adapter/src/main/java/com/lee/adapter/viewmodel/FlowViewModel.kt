package com.lee.adapter.viewmodel

import com.lee.adapter.entity.Content
import com.lee.adapter.entity.Page
import com.lee.adapter.repository.FlowRepository
import com.lee.library.extensions.bindLive
import com.lee.library.mvvm.base.BaseLiveData
import com.lee.library.mvvm.base.BaseViewModel
import com.lee.library.mvvm.load.LoadStatus
import com.lee.library.mvvm.load.PageNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
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

}