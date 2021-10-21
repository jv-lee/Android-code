package com.lee.library.mvvm.ui

import androidx.lifecycle.LiveData

/**
 * @author jv.lee
 * @date 2021/9/18
 * @description
 */
class UiStateLiveData(state: Int = INIT) : LiveData<Int>(state) {

    companion object {
        const val INIT = 0x00
        const val RELOAD = 0x01
    }

    fun init() {
        value = INIT
    }

    fun reload() {
        value = RELOAD
    }

}