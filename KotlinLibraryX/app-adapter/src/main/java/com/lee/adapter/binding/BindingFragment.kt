package com.lee.adapter.binding

import com.lee.adapter.databinding.FragmentBindingBinding
import com.lee.adapter.databinding.LayoutTextBinding
import com.lee.adapter.databinding.LayoutViewBinding
import com.lee.library.base.BaseBindingFragment
import com.lee.library.extensions.binding
import com.lee.library.extensions.inflate
import com.lee.library.extensions.show

/**
 *
 * @author jv.lee
 * @date 2021/6/15
 */
class BindingFragment : BaseBindingFragment<FragmentBindingBinding>() {

    private val includeBinding by binding(LayoutTextBinding::bind)

    private val headerBinding by inflate {
        LayoutViewBinding.inflate(it, mBinding.root, false)
    }

    private val bindingDialog by lazy { BindingDialogFragment() }

    override fun bindView() {
        mBinding.root.addView(headerBinding.root)
        mBinding.tvText.text = "LIJIAWEI"
        includeBinding.tvLayoutText.text = "TOKEYO"

        headerBinding.root.setOnClickListener {
            show(bindingDialog)
        }
    }

    override fun bindData() {
    }
}