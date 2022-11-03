package com.lee.library.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lee.library.R
import com.lee.library.extensions.dp2px

/**
 * SheetFragment通用基础类
 * @author jv.lee
 */
abstract class BaseSheetFragment(
    private val resourceId: Int? = 0,
    private var isFullWindow: Boolean = false,
    private var behaviorState: Int = BottomSheetBehavior.STATE_EXPANDED,
    private var peekHeight: Int = -1
) : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState) as BottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(inflater, container)
    }

    override fun onStart() {
        super.onStart()
        if (isFullWindow) {
            val bottomSheet = dialog?.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        getBehavior()?.peekHeight = requireContext().dp2px(peekHeight).toInt()
        getBehavior()?.state = behaviorState
    }

    open fun getBehavior(): BottomSheetBehavior<*>? {
        return BottomSheetBehavior.from(view?.parent as View)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
        bindData()
    }

    open fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        if (resourceId == null || resourceId == 0) {
            throw RuntimeException(
                "fragment createView() not override && constructor params resourceId == 0"
            )
        }
        return inflater.inflate(resourceId, container, false)
    }

    /**
     * 设置view基础配置
     */
    protected abstract fun bindView()

    /**
     * 设置加载数据等业务操作
     */
    protected abstract fun bindData()
}
