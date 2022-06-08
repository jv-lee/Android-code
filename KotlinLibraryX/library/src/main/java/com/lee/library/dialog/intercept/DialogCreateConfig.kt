package com.lee.library.dialog.intercept

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager

/**
 * Dialog拦截器创建配置
 * @param context 上下文
 * @param fragmentManager 管理类　
 * @param isShow 是否显示
 * @param bundle 透传参数
 * @author jv.lee
 * @date 2021/8/26
 */
data class DialogCreateConfig(
    val context: Context,
    val fragmentManager: FragmentManager? = null,
    var isShow: Boolean = true,
    var bundle: Bundle? = null
)