package com.lee.library.dialog.intercept

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager

/**
 * @author jv.lee
 * @date 2021/8/26
 * @description
 */
data class DialogCreateConfig(
    val context: Context,
    val fragmentManager: FragmentManager? = null,
    var isShow: Boolean = true,
    var bundle: Bundle? = null
)