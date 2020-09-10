package com.lee.library.dialog.core

import android.content.Context
import com.lee.library.R
import com.lee.library.dialog.core.BaseDialog
import com.lee.library.dialog.core.setBottomDialog

/**
 * @author jv.lee
 * @date 2020/9/10
 * @description 使用 setBottomDialog(260) 限制子类最大高度
 */
abstract class BaseBottomDialog constructor(context: Context) :
    BaseDialog(context, R.style.BottomDialogTheme)