package com.lee.api.fragment

import android.content.Intent
import com.lee.api.databinding.FragmentResultBinding
import com.lee.library.base.BaseBindingFragment

class ResultFragment : BaseBindingFragment<FragmentResultBinding>() {

    override fun bindView() {
        requireActivity().setResult(0, Intent().putExtra("value", "this is result data"))
    }

    override fun bindData() {
    }
}