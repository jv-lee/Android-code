package com.lee.app.viewmodel

import android.app.Application
import android.databinding.ObservableField
import android.graphics.drawable.Drawable
import com.lee.app.App
import com.lee.app.R
import com.lee.app.base.BaseViewModel
import com.lee.app.model.entity.User
import com.lee.app.model.repository.MainRepository

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
class MainViewModel(application: Application) : BaseViewModel(application) {

    var model: MainRepository = MainRepository()
    var user: ObservableField<User> = ObservableField()


    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun test(): Drawable {
        return getApplication<App>().getDrawable(R.mipmap.ic_launcher)
    }
}