package com.lee.library.base

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.lee.library.utils.StatusUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

/**
 * @author jv.lee
 * @date 2021/6/15
 * @description
 */
abstract class BaseActivity :
    AppCompatActivity(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private var permissionSuccessCall: (() -> Unit)? = null
    private var permissionFailedCall: ((String) -> Unit)? = null

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { it ->
            if (it) permissionSuccessCall?.invoke() else permissionFailedCall?.invoke("")
        }

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
            it.forEach {
                if (!it.value) {
                    permissionFailedCall?.invoke(it.key)
                    return@forEach
                }
            }
            permissionSuccessCall?.invoke()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusUtil.statusBar(window, false)
        super.onCreate(savedInstanceState)

        initSavedState(intent, savedInstanceState)

        bindView()

        bindData()
    }

    open fun initSavedState(intent: Intent, savedInstanceState: Bundle?) {}

    protected abstract fun bindView()

    protected abstract fun bindData()

    @ExperimentalCoroutinesApi
    override fun onDestroy() {
        super.onDestroy()
        cancel()
        permissionLauncher.unregister()
        permissionsLauncher.unregister()
    }

    fun requestPermission(
        permission: String,
        successCall: () -> Unit,
        failedCall: (String) -> Unit = {}
    ) {
        this@BaseActivity.permissionSuccessCall = successCall
        this@BaseActivity.permissionFailedCall = failedCall
        permissionLauncher.launch(permission)
    }

    fun requestPermissions(
        vararg permission: String,
        successCall: () -> Unit,
        failedCall: (String) -> Unit = {}
    ) {
        this@BaseActivity.permissionSuccessCall = successCall
        this@BaseActivity.permissionFailedCall = failedCall
        permissionsLauncher.launch(permission)
    }

}