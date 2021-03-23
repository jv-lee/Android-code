package com.lee.camerax.base

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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

}