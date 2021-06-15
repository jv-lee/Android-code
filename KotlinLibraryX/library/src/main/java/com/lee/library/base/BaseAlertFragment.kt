package com.lee.library.base

import com.lee.library.R

/**
 * @author jv.lee
 * @date 2020/9/21
 * @description 通用AlertDialogFragment 基础类 带Alert缩放动画
 */
abstract class BaseAlertFragment(resourceId: Int, isCancel: Boolean = true) :
    BaseDialogFragment(resourceId, isCancel) {
    init {
        setStyle(STYLE_NO_TITLE, R.style.BaseAlertDialog)
    }
}