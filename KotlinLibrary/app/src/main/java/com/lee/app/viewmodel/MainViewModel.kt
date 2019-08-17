package com.lee.app.viewmodel

import android.databinding.ObservableField
import com.lee.app.base.BaseViewModel
import com.lee.app.model.entity.User
import com.lee.app.model.repository.MainRepository

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
class MainViewModel : BaseViewModel() {

    var model: MainRepository = MainRepository()
    var user: ObservableField<User> = ObservableField()


}