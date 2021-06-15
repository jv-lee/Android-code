package com.lee.api.fragment

import androidx.activity.OnBackPressedCallback
import com.lee.api.R
import com.lee.api.databinding.FragmentBackBinding
import com.lee.library.base.BaseVMFragment
import com.lee.library.mvvm.base.BaseViewModel


class BackFragment : BaseVMFragment<FragmentBackBinding, BaseViewModel>(R.layout.fragment_back) {
    override fun bindView() {
        requireActivity().onBackPressedDispatcher.addCallback(this,
            //enabled true 为fragment 拦截back事件->传递至 handleOnBackPressed()
            object : OnBackPressedCallback(true) {
                //在该方法中做条件判断
                override fun handleOnBackPressed() {
                    if (true) {
                        activity?.finish()
                    }
                }
            })
    }

    override fun bindData() {

    }

}