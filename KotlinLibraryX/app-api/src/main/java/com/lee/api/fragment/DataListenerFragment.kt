package com.lee.api.fragment

import androidx.fragment.app.FragmentResultListener
import com.lee.api.R
import com.lee.api.databinding.FragmentDataListenerBinding
import com.lee.library.base.BaseFragment
import com.lee.library.mvvm.base.BaseViewModel


class DataListenerFragment :
    BaseFragment<FragmentDataListenerBinding, BaseViewModel>(R.layout.fragment_data_listener) {
    override fun bindView() {
        parentFragmentManager.setFragmentResultListener(
            "key",
            this,
            FragmentResultListener { _, result ->
                toast(result.getString("data"))
            })
    }

    override fun bindData() {

    }

}