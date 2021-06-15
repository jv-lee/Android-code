package com.lee.api.fragment

import com.lee.api.R
import com.lee.api.databinding.FragmentDataListenerBinding
import com.lee.library.base.BaseVMFragment
import com.lee.library.mvvm.base.BaseViewModel


class DataListenerFragment :
    BaseVMFragment<FragmentDataListenerBinding, BaseViewModel>(R.layout.fragment_data_listener) {
    override fun bindView() {
        //androidx.fragment:fragment-ktx:1.3.0-alpha04
//        parentFragmentManager.setFragmentResultListener(
//            "key",
//            this,
//            FragmentResultListener { _, result ->
//                toast(result.getString("data"))
//            })
    }

    override fun bindData() {

    }

}