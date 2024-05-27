package com.lee.api.fragment

import com.lee.api.databinding.FragmentDataListenerBinding
import com.lee.library.base.BaseBindingFragment

class DataListenerFragment : BaseBindingFragment<FragmentDataListenerBinding>() {
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