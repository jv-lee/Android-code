package com.lee.api.fragment

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.lee.api.R
import com.lee.api.databinding.FragmentResultBinding
import com.lee.library.base.BaseVMFragment

class ResultFragment :
    BaseVMFragment<FragmentResultBinding, ViewModel>(R.layout.fragment_result) {

    override fun bindView() {
        requireActivity().setResult(0, Intent().putExtra("value", "this is result data"))
    }

    override fun bindData() {
    }


}