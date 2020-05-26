package com.lee.app.viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.graphics.drawable.Drawable
import com.lee.app.App
import com.lee.app.R
import com.lee.app.model.entity.User
import com.lee.app.model.repository.MainRepository
import com.lee.library.mvvm.base.BaseViewModel

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
class MainViewModel(application: Application) : BaseViewModel(application) {

    private val model by lazy { MainRepository() }
    val userInfo: MutableLiveData<User> = MutableLiveData()
    val user: ObservableField<User> = ObservableField()

    fun login(id: Int) {
        userInfo.value = model.getUserInfo(id).value
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun test(): Drawable {
        return getApplication<App>().getDrawable(R.mipmap.ic_launcher)
    }

}