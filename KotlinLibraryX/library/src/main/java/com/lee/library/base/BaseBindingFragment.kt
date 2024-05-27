package com.lee.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lee.library.tools.ViewBindingTools

abstract class BaseBindingFragment<VB : ViewBinding> : BaseFragment() {

    private var _binding: VB? = null
    val mBinding: VB get() = _binding!!
    open val contentView: View get() = mBinding.root

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewBindingTools.inflateWithGeneric(this, inflater, container, false)
        return contentView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}