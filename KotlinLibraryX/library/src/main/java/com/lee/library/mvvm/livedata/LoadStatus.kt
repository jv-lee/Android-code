package com.lee.library.mvvm.livedata

import androidx.annotation.IntDef
import androidx.lifecycle.LiveData
import com.lee.library.mvvm.livedata.LoadStatus.Companion.INIT
import com.lee.library.mvvm.livedata.LoadStatus.Companion.LOAD_MORE
import com.lee.library.mvvm.livedata.LoadStatus.Companion.REFRESH
import com.lee.library.mvvm.livedata.LoadStatus.Companion.RELOAD

/**
 * @author jv.lee
 * @date 2020/11/25
 * @description
 */
@IntDef(REFRESH, LOAD_MORE, RELOAD)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class LoadStatus {

    companion object {
        const val INIT: Int = 0x0000
        const val REFRESH: Int = 0x001
        const val LOAD_MORE: Int = 0x002
        const val RELOAD: Int = 0x003
    }
}

interface LoadService {
    fun init()
    fun reload()
    fun refresh()
    fun loadMore()
}

class LoadStatusLiveData(status: Int = INIT) : LiveData<Int>(status), LoadService  {

    override fun init() {
        value = INIT
    }

    override fun reload() {
        value = RELOAD
    }

    override fun refresh() {
        value = REFRESH
    }

    override fun loadMore() {
        value = LOAD_MORE
    }

}

class LoadPageLiveData(var initPage: Int = 0) : LiveData<Int>(), LoadService {

    private var page = initPage

    private fun load(@LoadStatus status: Int) {
        value = when (status) {
            INIT, REFRESH -> {
                page = initPage
                page
            }
            RELOAD -> {
                page
            }
            LOAD_MORE -> {
                ++page
            }
            else -> page
        }
    }

    override fun init() {
        load(INIT)
    }

    override fun reload() {
        load(RELOAD)
    }

    override fun refresh() {
        load(REFRESH)
    }

    override fun loadMore() {
        load(LOAD_MORE)
    }

}