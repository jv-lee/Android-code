package com.lee.api.fragment

import com.lee.api.databinding.FragmentDataSendBinding
import com.lee.library.base.BaseBindingFragment

class DataSendFragment : BaseBindingFragment<FragmentDataSendBinding>() {
    override fun bindView() {
        mBinding.btnSend.setOnClickListener {
            // androidx.fragment:fragment-ktx:1.3.0-alpha04
//            parentFragmentManager.setFragmentResult("key", Bundle().apply {
//                putString("data", "this is fragment send data.")
//            })
        }
    }

    override fun bindData() {
    }
}