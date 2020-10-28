package com.lee.library.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.lee.library.extensions.getVmClass
import com.lee.library.mvvm.base.BaseViewModel
import com.lee.library.utils.ActivityUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

/**
 * @author jv.lee
 * @date 2019/8/16.
 * @description
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel>(
    var layoutId: Int
) : Fragment()
    , CoroutineScope by CoroutineScope(Dispatchers.Main) {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    private var fistVisible = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //设置viewBinding
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //设置viewModel
        try {
            viewModel = ViewModelProvider(this).get(getVmClass(this))
        } catch (e: Exception) {
        }
        intentParams(arguments, savedInstanceState)
        bindView()
        bindData()
        initFailedViewModel()
    }

    protected fun initFailedViewModel() {
        viewModel.failedEvent.observe(viewLifecycleOwner, Observer {
            toast(it.message)
        })
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

    /**
     * 初始化参数传递
     */
    open fun intentParams(arguments: Bundle?, savedInstanceState: Bundle?) {}

    /**
     * 设置view基础配置
     */
    protected abstract fun bindView()

    /**
     * 设置加载数据等业务操作
     *
     */
    protected abstract fun bindData()


    /**
     * 使用page 多fragment时 懒加载
     */
    open fun lazyLoad() {}

    private fun getChildClassName(): String {
        return javaClass.simpleName
    }

    /**
     * 创建ViewModel
     */
    protected fun <T : ViewModel> createViewModel(cls: Class<T>): T {
        return ViewModelProviders.of(this).get(cls)
    }

    fun Fragment.toast(message: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
        message ?: return
        Toast.makeText(activity, message, duration).show()
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

}