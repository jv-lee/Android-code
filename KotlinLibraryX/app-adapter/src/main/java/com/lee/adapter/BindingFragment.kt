package com.lee.adapter

import com.lee.adapter.databinding.FragmentBindingBinding
import com.lee.library.base.BaseFragment
import com.lee.library.extensions.binding

/**
 * @author jv.lee
 * @date 2021/6/15
 * @description
 */
class BindingFragment : BaseFragment(R.layout.fragment_binding) {

    val binding by binding(FragmentBindingBinding::bind)

    override fun bindView() {
        binding.tvText.text = "LIJIAWEI"
    }

    override fun bindData() {

    }

}