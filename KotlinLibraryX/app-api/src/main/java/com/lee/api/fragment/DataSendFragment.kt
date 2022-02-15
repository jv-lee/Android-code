package com.lee.api.fragment

import androidx.lifecycle.ViewModel
import com.lee.api.R
import com.lee.api.databinding.FragmentDataSendBinding
import com.lee.library.base.BaseVMFragment

class DataSendFragment :
    BaseVMFragment<FragmentDataSendBinding, ViewModel>(R.layout.fragment_data_send) {
    override fun bindView() {
        binding.btnSend.setOnClickListener {
            //androidx.fragment:fragment-ktx:1.3.0-alpha04
//            parentFragmentManager.setFragmentResult("key", Bundle().apply {
//                putString("data", "this is fragment send data.")
//            })
        }
    }

    override fun bindData() {

    }

}