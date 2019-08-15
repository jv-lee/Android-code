package com.lee.library.viewmodel

import android.databinding.ObservableField
import com.lee.library.base.BaseViewModel
import com.lee.library.model.entity.User
import com.lee.library.model.repository.MainRepository

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
class MainViewModel : BaseViewModel() {

    var model: MainRepository = MainRepository()
    var user: ObservableField<User> = ObservableField()

}