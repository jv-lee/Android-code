package com.simple.vm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lee.library.utils.LogUtil
import com.simple.vm.constants.USER_ID_KEY

/**
 * 通过构造函数传递请求参数 的ViewModel实现
 * @author jv.lee
 * @date 8/19/21
 */
class HandleViewModel(handle: SavedStateHandle) : ViewModel() {

    private val userID = handle[USER_ID_KEY] ?: ""
    private val userType = handle["userType"] ?: ""

    val textLiveData by lazy { MutableLiveData<String>() }

    init {
        LogUtil.i("StateHandleViewModel init.")
        requestData()
    }

    // 页面初始拉取数据使用init初次创建后获取
    private fun requestData() {
        requestByNetwork(userID, userType)
    }

    private fun requestByNetwork(userID: String, userType: String) {
        textLiveData.postValue("HandlerViewModel -> notify:$userID - $userType")
    }
}