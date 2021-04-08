package com.lee.camerax.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * @author jv.lee
 * @date 2021/3/23
 * @description
 */
abstract class BaseFragment<Binding : ViewBinding> : Fragment() {
    protected var binding: Binding? = null

    private var permissionSuccessCall: (() -> Unit)? = null
    private var permissionFailedCall: ((String) -> Unit)? = null

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { it ->
            if (it) permissionSuccessCall?.invoke() else permissionFailedCall?.invoke("")
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = bindViewBinding()
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    abstract fun bindViewBinding(): Binding

    abstract fun bindView()

    fun Fragment.toast(message: String?) {
        message?.let { Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show() }
    }

    fun Fragment.requestPermission(
        permission: String,
        successCall: () -> Unit,
        failedCall: (String) -> Unit = {}
    ) {
        permissionSuccessCall = successCall
        permissionFailedCall = failedCall
        permissionLauncher.launch(permission)
    }
}