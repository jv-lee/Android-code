package com.lee.api.tools

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

/**
 * @author jv.lee
 * @date 2021/6/15

 */
fun <VB : ViewBinding> Activity.binding(inflate: (LayoutInflater) -> VB) = lazy {
    inflate(layoutInflater).apply { setContentView(root) }
}