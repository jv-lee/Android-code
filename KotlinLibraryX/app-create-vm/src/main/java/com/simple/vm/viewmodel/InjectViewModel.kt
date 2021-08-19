package com.simple.vm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lee.library.utils.LogUtil
import com.simple.vm.constants.USER_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author jv.lee
 * @data 2021/8/19
 * @description
 */
@HiltViewModel
class InjectViewModel @Inject constructor(handle: SavedStateHandle) : ViewModel() {

    private val userID = handle[USER_ID_KEY] ?: ""

    val textLiveData by lazy { MutableLiveData<String>() }

    init {
        LogUtil.i("InjectViewModel init.")
        requestData()
    }

    //页面初始拉取数据使用init初次创建后获取
    fun requestData() {
        requestByNetwork(userID)
    }

    private fun requestByNetwork(userID: String) {
        textLiveData.postValue("InjectViewModel -> notify:$userID")
    }

}