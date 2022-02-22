package com.lee.adapter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lee.adapter.repository.ContentRepository
import com.lee.library.mvvm.annotation.LoadStatus
import com.lee.library.mvvm.ui.UiStatePage
import com.lee.library.mvvm.ui.pageLaunch
import com.lee.library.mvvm.vm.CoroutineViewModel
import kotlinx.coroutines.delay

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
class ContentViewModel : CoroutineViewModel() {

    private val repository by lazy { ContentRepository() }

    private val _dataLive = MutableLiveData<UiStatePage>(UiStatePage.Default(1))
    val dataLive:LiveData<UiStatePage> = _dataLive

    fun loadData(@LoadStatus status: Int) {
        launchMain {
            delay(500)
            _dataLive.pageLaunch(status, requestBlock = { page ->
                repository.getContent(page)
            })
        }
    }

}