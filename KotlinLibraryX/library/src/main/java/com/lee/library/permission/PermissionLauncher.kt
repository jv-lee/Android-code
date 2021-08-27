package com.lee.library.permission

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lee.library.extensions.destroy

/**
 * @author jv.lee
 * @data 2021/8/27
 * @description 权限申请启动器
 */
class PermissionLauncher {

    constructor(fragment: Fragment) {
        createLauncher(fragment)
    }

    constructor(activity: FragmentActivity){
        createLauncher(activity)
    }

    private var permissionSuccessCall: (() -> Unit)? = null
    private var permissionFailedCall: ((String) -> Unit)? = null

    private var permissionLauncher: ActivityResultLauncher<String>? = null
    private var permissionsLauncher: ActivityResultLauncher<Array<out String>>? = null

    private fun createLauncher(thisClass: Any) {
        val thisT = when (thisClass) {
            is FragmentActivity -> {
                thisClass
            }
            is Fragment -> {
                thisClass
            }
            else -> {
                throw ClassCastException("thisClass type not is FragmentActivity/Fragment")
            }
        }

        permissionLauncher =
            thisT.registerForActivityResult(ActivityResultContracts.RequestPermission()) { it ->
                if (it) permissionSuccessCall?.invoke() else permissionFailedCall?.invoke("")
            }
        permissionsLauncher =
            thisT.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
                it.forEach {
                    if (!it.value) {
                        permissionFailedCall?.invoke(it.key)
                        return@forEach
                    }
                }
                permissionSuccessCall?.invoke()
            }

        thisT.lifecycle.destroy {
            permissionLauncher?.unregister()
            permissionsLauncher?.unregister()
            permissionSuccessCall = null
            permissionFailedCall = null
        }
    }

    fun requestPermission(
        permission: String,
        successCall: () -> Unit,
        failedCall: (String) -> Unit = {}
    ) {
        permissionSuccessCall = successCall
        permissionFailedCall = failedCall
        permissionLauncher?.launch(permission)
    }

    fun requestPermissions(
        vararg permission: String,
        successCall: () -> Unit,
        failedCall: (String) -> Unit = {}
    ) {
        permissionSuccessCall = successCall
        permissionFailedCall = failedCall
        permissionsLauncher?.launch(permission)
    }
}

