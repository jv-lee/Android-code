package com.lee.adapter.viewmodel

import com.lee.adapter.repository.ContentRepository
import com.lee.library.mvvm.livedata.LoadStatus
import com.lee.library.mvvm.ui.UiStatePageLiveData
import com.lee.library.mvvm.viewmodel.CoroutineViewModel
import kotlinx.coroutines.delay

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
class ContentViewModel : CoroutineViewModel() {

    private val repository by lazy { ContentRepository() }

    val dataLive = UiStatePageLiveData(1)

    fun loadData(@LoadStatus status: Int) {
        launchMain {
            delay(500)
            dataLive.pageLaunch(status, networkBlock = { page ->
                repository.getContent(page)
            })
        }
    }

}