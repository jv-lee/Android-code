package com.simple.vm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lee.library.utils.LogUtil

/**
 * @author jv.lee
 * @date 8/19/21
 * @description 通过构造函数传递请求参数 的ViewModel实现
 */
class ParamsViewModel(private val userID: String) : ViewModel() {

    init {
        LogUtil.i("ParamsViewModel init.")
        requestData()
    }

    val textLiveData = MutableLiveData<String>()

    //页面初始拉取数据使用init初次创建后获取
    fun requestData() {
        requestByNetwork(userID)
    }

    private fun requestByNetwork(userID: String) {
        textLiveData.postValue("ParamsViewModel -> notify:$userID")
    }

    /**
     * 对外提供有参构造器 通过该工厂构造放入当前activity/fragment viewModelStore中存储
     */
    class CreateFactory(private val userID: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java).newInstance(userID)
        }
    }
}