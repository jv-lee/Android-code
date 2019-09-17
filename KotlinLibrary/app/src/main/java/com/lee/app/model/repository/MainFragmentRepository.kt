package com.lee.app.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.lee.library.mvvm.IModel

/**
 * @author jv.lee
 * @date 2019/9/17.
 * @description
 */
class MainFragmentRepository : IModel {

    fun getData(page: Int): LiveData<List<Int>> {
        val data = MutableLiveData<List<Int>>()
        val array = ArrayList<Int>()

        for (index in page..(page + 10)) {
            array.add(index)
        }
        data.value = array.toList()
        return data
    }

}