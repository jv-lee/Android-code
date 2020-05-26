package com.lee.app.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.lee.app.model.repository.MainFragmentRepository
import com.lee.library.mvvm.base.BaseViewModel

/**
 * @author jv.lee
 * @date 2019/9/17.
 * @description
 */
class MainFragmentViewModel(application: Application) : BaseViewModel(application) {

    val model by lazy { MainFragmentRepository() }
    val data by lazy { MutableLiveData<List<Int>>() }

    fun getData(page: Int) {
        model.getData(page).observeForever {
            data.value = it
        }
    }

}