package com.lee.library.mvvm.load

import androidx.lifecycle.LiveData

/**
 * @author jv.lee
 * @date 2021/9/18
 * @description
 */
class LoadStatusLiveData(status: Int = LoadStatus.INIT) : LiveData<Int>(status) {

    fun init() {
        value = LoadStatus.INIT
    }

    fun reload() {
        value = LoadStatus.RELOAD
    }

    fun refresh() {
        value = LoadStatus.REFRESH
    }

    fun loadMore() {
        value = LoadStatus.LOAD_MORE
    }

}