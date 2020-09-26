package com.lee.library.dialog.core

import android.content.Context
import com.lee.library.R

/**
 * @author jv.lee
 * @date 2020-03-07
 * @description 自定义dialog 超类
 */
abstract class BaseTranslucentDialog(context: Context,cancel:Boolean=false) :
    BaseDialog(context, R.style.BaseTranslucentDialog,cancel)