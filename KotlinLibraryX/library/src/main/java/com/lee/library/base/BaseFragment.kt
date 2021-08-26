package com.lee.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

/**
 * @author jv.lee
 * @date 2019/8/16.
 * @description
 */
abstract class BaseFragment(private val resourceId: Int? = 0) : Fragment(),
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private var fistVisible = true

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
        bindData()
    }

    override fun onResume() {
        super.onResume()
        if (fistVisible) {
            fistVisible = false
            lazyLoad()
        }
    }

    @ExperimentalCoroutinesApi
    override fun onDetach() {
        super.onDetach()
        cancel()
        permissionLauncher.unregister()
        permissionsLauncher.unregister()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onPause() {
        super.onPause()
        if (requireActivity().isFinishing) {
            dispose()
        }
    }

    /**
     * 解决fragment中 onDestroy等函数回调过慢时 使用该方法解除引用
     */
    open fun dispose() {
    }

    open fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        if (resourceId == null || resourceId == 0) throw RuntimeException("fragment createView() not override && constructor params resourceId == 0")
        return inflater.inflate(resourceId, container, false)
    }

    /**
     * 设置view基础配置
     */
    protected abstract fun bindView()

    /**
     * 设置加载数据等业务操作
     */
    protected abstract fun bindData()

    /**
     * 使用page 多fragment时 懒加载
     */
    open fun lazyLoad() {}

    private fun getChildClassName(): String {
        return javaClass.simpleName
    }

    fun requestPermission(
        permission: String,
        successCall: () -> Unit,
        failedCall: (String) -> Unit = {}
    ) {
        this@BaseFragment.permissionSuccessCall = successCall
        this@BaseFragment.permissionFailedCall = failedCall
        permissionLauncher.launch(permission)
    }

    fun requestPermissions(
        vararg permission: String,
        successCall: () -> Unit,
        failedCall: (String) -> Unit = {}
    ) {
        this@BaseFragment.permissionSuccessCall = successCall
        this@BaseFragment.permissionFailedCall = failedCall
        permissionsLauncher.launch(permission)
    }

}