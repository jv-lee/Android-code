package com.lee.adapter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lee.adapter.repository.ContentRepository
import com.lee.library.viewstate.LoadStatus
import com.lee.library.viewstate.UiStatePage
import com.lee.library.viewstate.pageLaunch
import com.lee.library.viewmodel.CoroutineViewModel
import kotlinx.coroutines.delay

/**
 *
 * @author jv.lee
 * @date 2020/11/25
 */
class ContentViewModel : CoroutineViewModel() {

    private val repository by lazy { ContentRepository() }

    private val _dataLive = MutableLiveData<UiStatePage>(UiStatePage.Default(1))
    val dataLive: LiveData<UiStatePage> = _dataLive

    fun loadData(@LoadStatus status: Int) {
        launchMain {
            delay(500)
            _dataLive.pageLaunch(status, requestBlock = { page ->
                repository.getContent(page)
            })
        }
    }
}