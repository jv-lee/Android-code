package com.lee.camerax.base

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding

/**
 * @author jv.lee
 * @date 2021/3/23
 * @description
 */
abstract class BaseActivity<Binding : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: Binding

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
        super.onCreate(savedInstanceState)
        binding = bindViewBinding()
        setContentView(binding.root)
        bindView()
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionLauncher.unregister()
    }

    abstract fun bindViewBinding(): Binding

    abstract fun bindView()

    fun FragmentActivity.toast(message: String?) {
        message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
    }

    fun FragmentActivity.requestPermission(
        permission: String,
        successCall: () -> Unit,
        failedCall: (String) -> Unit = {}
    ) {
        this@BaseActivity.permissionSuccessCall = successCall
        this@BaseActivity.permissionFailedCall = failedCall
        permissionLauncher.launch(permission)
    }

    fun FragmentActivity.requestPermissions(
        vararg permission: String,
        successCall: () -> Unit,
        failedCall: (String) -> Unit = {}
    ) {
        this@BaseActivity.permissionSuccessCall = successCall
        this@BaseActivity.permissionFailedCall = failedCall
        permissionsLauncher.launch(permission)
    }

}