package com.lee.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lee.library.tools.ViewBindingTools

/**
 * 通过反射实现binding注入 baseSheetFragment
 * @author jv.lee
 * @date 2024/6/6
 */
abstract class BaseBindingSheetFragment<VB : ViewBinding>(
    isFullWindow: Boolean = false,
    behaviorState: Int = BottomSheetBehavior.STATE_EXPANDED,
    peekHeight: Int = -1
) : BaseSheetFragment(
    isFullWindow = isFullWindow,
    behaviorState = behaviorState,
    peekHeight = peekHeight
) {

    private var _binding: VB? = null
    val mBinding: VB get() = _binding!!
    open val contentView: View get() = mBinding.root

    override fun getBehavior(): BottomSheetBehavior<*>? {
        return BottomSheetBehavior.from(mBinding.root.parent as View)
    }

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