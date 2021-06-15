package com.lee.api.fragment

import android.content.Intent
import com.lee.api.R
import com.lee.api.databinding.FragmentResultBinding
import com.lee.api.tools.viewBinding
import com.lee.library.base.BaseVMFragment
import com.lee.library.mvvm.base.BaseViewModel

class ResultFragment :
    BaseVMFragment<FragmentResultBinding, BaseViewModel>(R.layout.fragment_result) {

    override fun bindView() {
        requireActivity().setResult(0, Intent().putExtra("value", "this is result data"))
    }

    override fun bindData() {
    }


}