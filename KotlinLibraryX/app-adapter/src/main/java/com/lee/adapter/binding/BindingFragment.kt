package com.lee.adapter.binding

import com.lee.adapter.R
import com.lee.adapter.databinding.FragmentBindingBinding
import com.lee.adapter.databinding.LayoutTextBinding
import com.lee.adapter.databinding.LayoutViewBinding
import com.lee.library.base.BaseFragment
import com.lee.library.extensions.binding
import com.lee.library.extensions.inflate

/**
 * @author jv.lee
 * @date 2021/6/15
 * @description
 */
class BindingFragment : BaseFragment(R.layout.fragment_binding) {

    private val binding by binding(FragmentBindingBinding::bind)
    private val includeBinding by binding(LayoutTextBinding::bind)

    private val headerBinding by inflate {
        LayoutViewBinding.inflate(it, binding.root, false)
    }

    private val bindingDialog by lazy { BindingDialogFragment() }

    override fun bindView() {
        binding.root.addView(headerBinding.root)
        binding.tvText.text = "LIJIAWEI"
        includeBinding.tvLayoutText.text = "TOKEYO"

        headerBinding.root.setOnClickListener {
            show(bindingDialog)
        }
    }

    override fun bindData() {

    }

}