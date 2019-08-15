package com.lee.library.view.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.lee.library.R
import com.lee.library.base.BaseActivity
import com.lee.library.databinding.ActivityMainBinding
import com.lee.library.viewmodel.MainViewModel


class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main, MainViewModel::class.java) {

    private var id: Int = 1994

    override fun bindView() {
        binding.setClick {
            notificationData()
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        notificationData()
    }

    private fun notificationData() {
        id++
        viewModel.model.getUserInfo(id).observe(this, Observer {
            viewModel.user.set(it)
        })
    }

}



