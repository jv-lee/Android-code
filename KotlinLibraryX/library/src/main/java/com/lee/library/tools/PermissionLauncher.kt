package com.lee.library.tools

import android.annotation.TargetApi
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lee.library.extensions.destroy

/**
 * 权限申请启动器
 * @author jv.lee
 * @date 2021/8/27
 */
@TargetApi(Build.VERSION_CODES.M)
class PermissionLauncher {

    constructor(fragment: Fragment) {
        checkPermission = {
            fragment.shouldShowRequestPermissionRationale(it)
        }
        createLauncher(fragment)
    }

    constructor(activity: FragmentActivity) {
        checkPermission = {
            activity.shouldShowRequestPermissionRationale(it)
        }
        createLauncher(activity)
    }

    //权限校验禁止状态函数
    private var checkPermission: ((String) -> Boolean)

    //单个权限申请临时存储变量
    private var singlePermission: String = ""

    private var permissionSuccessCall: (() -> Unit)? = null
    private var permissionCancelCall: ((String) -> Unit)? = null
    private var permissionDisableCall: ((String) -> Unit)? = null

    private var permissionLauncher: ActivityResultLauncher<String>? = null
    private var permissionsLauncher: ActivityResultLauncher<Array<String>>? = null

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
            thisT.registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionEnable ->
                if (permissionEnable) {
                    permissionSuccessCall?.invoke()
                    return@registerForActivityResult
                }

                if (checkPermission.invoke(singlePermission)) {
                    permissionCancelCall?.invoke(singlePermission)
                } else {
                    permissionDisableCall?.invoke(singlePermission)
                }
            }

        permissionsLauncher =
            thisT.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionInfoMap ->
                permissionInfoMap.forEach { permissionInfo ->
                    if (!permissionInfo.value) {
                        if (checkPermission.invoke(permissionInfo.key)) {
                            permissionCancelCall?.invoke(permissionInfo.key)
                        } else {
                            permissionDisableCall?.invoke(permissionInfo.key)
                        }
                        return@registerForActivityResult
                    }
                }
                permissionSuccessCall?.invoke()
            }

        thisT.lifecycle.destroy {
            permissionLauncher?.unregister()
            permissionsLauncher?.unregister()
            permissionSuccessCall = null
            permissionCancelCall = null
            permissionDisableCall = null
        }
    }

    /**
     * 单个权限申请
     * @param permission 被申请的权限常量
     * @param successCall 权限申请成功回调
     * @param cancelCall 用户点击取消回调
     * @param disableCall 用户勾选禁止提示申请权限并拒绝回调
     */
    fun requestPermission(
        permission: String,
        successCall: () -> Unit,
        cancelCall: (String) -> Unit = {},
        disableCall: (String) -> Unit = {}
    ) {
        singlePermission = permission
        permissionSuccessCall = successCall
        permissionCancelCall = cancelCall
        permissionDisableCall = disableCall
        permissionLauncher?.launch(permission)
    }

    /**
     * 多个权限申请
     * @param permissions 被申请的权限常量
     * @param successCall 权限申请成功回调
     * @param cancelCall 用户点击取消回调
     * @param disableCall 用户勾选禁止提示申请权限并拒绝回调
     */
    fun requestPermissions(
        permissions: Array<String>,
        successCall: () -> Unit,
        cancelCall: (String) -> Unit = {},
        disableCall: (String) -> Unit = {}
    ) {
        permissionSuccessCall = successCall
        permissionCancelCall = cancelCall
        permissionDisableCall = disableCall
        permissionsLauncher?.launch(permissions)
    }
}

