package com.lee.api.fragment

import androidx.lifecycle.ViewModel
import com.lee.api.R
import com.lee.api.databinding.FragmentDataListenerBinding
import com.lee.library.base.BaseVMFragment

class DataListenerFragment :
    BaseVMFragment<FragmentDataListenerBinding, ViewModel>(R.layout.fragment_data_listener) {
    override fun bindView() {
        // androidx.fragment:fragment-ktx:1.3.0-alpha04
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