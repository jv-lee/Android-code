package com.lee.library.extensions

import android.app.Dialog
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.lee.library.utils.ActivityUtil

/**
 * @author jv.lee
 * @data 2021/8/26
 * @description
 */
fun Fragment.toast(message: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    message ?: return
    Toast.makeText(requireContext().applicationContext, message, duration).show()
}

fun Fragment.show(dialog: Dialog) {
    if (ActivityUtil.assertActivityDestroyed(requireActivity())) return
    try {
        dialog.show()
    } catch (e: Exception) {
    }
}

fun Fragment.dismiss(dialog: Dialog) {
    if (ActivityUtil.assertActivityDestroyed(requireActivity())) return
    try {
        dialog.dismiss()
    } catch (e: Exception) {
    }
}

fun Fragment.show(dialog: DialogFragment) {
    if (ActivityUtil.assertActivityDestroyed(requireActivity())) return
    try {
        dialog.show(childFragmentManager, dialog::class.java.simpleName)
    } catch (e: Exception) {
    }
}

fun Fragment.dismiss(dialog: DialogFragment) {
    if (ActivityUtil.assertActivityDestroyed(requireActivity())) return
    try {
        dialog.dismiss()
    } catch (e: Exception) {
    }
}