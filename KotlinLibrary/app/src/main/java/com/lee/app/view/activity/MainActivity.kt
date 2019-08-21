package com.lee.app.view.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.lee.app.R
import com.lee.library.base.BaseActivity
import com.lee.app.databinding.ActivityMainBinding
import com.lee.app.view.fragment.MainFragment
import com.lee.app.view.fragment.MyBottomSheetFragment
import com.lee.app.viewmodel.MainViewModel


class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main, MainViewModel::class.java) {

    private var id: Int = 1994

    override fun bindView() {
        binding.setClick {
            when (it.id) {
                R.id.button -> {
                    supportFragmentManager.beginTransaction().add(R.id.frame_container, MainFragment()).commit()
                }
                R.id.button2 -> {
                    MyBottomSheetFragment().show(supportFragmentManager, "dialog")
                }
            }

        }

    }

    override fun bindData(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        notificationData()
        toast(viewModel.test().toString())
    }

    private fun notificationData() {
        id++
        viewModel.model.getUserInfo(id).observe(this, Observer {
            viewModel.user.set(it)
        })
    }

}



