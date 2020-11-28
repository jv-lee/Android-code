package com.lee.adapter.viewmodel

import com.lee.adapter.entity.ContentData
import com.lee.adapter.entity.PageData
import com.lee.adapter.repository.ContentRepository
import com.lee.library.mvvm.base.BaseViewModel
import com.lee.library.mvvm.load.LoadStatus
import com.lee.library.mvvm.live.PageLiveData
import kotlinx.coroutines.delay

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
class ContentViewModel : BaseViewModel() {

    private val repository by lazy { ContentRepository() }


    val dataLiveData by lazy { PageLiveData<PageData<ContentData>>(limit = 1) }

    fun loadData(@LoadStatus status: Int) {
        launchMain {
            delay(500)
            dataLiveData.pageLaunch(status, networkBlock = { page ->
                repository.getContent(page)
            })
        }
    }

}