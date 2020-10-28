package com.lee.library.base

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.lee.library.R
import com.lee.library.extensions.getVmClass
import com.lee.library.mvvm.base.BaseViewModel
import com.lee.library.utils.ActivityUtil
import com.lee.library.utils.StatusUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel>(
    var layoutId: Int
) :
    AppCompatActivity(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    private var firstTime: Long = 0
    private var hasBackExit = false
    private var hasBanBack = false
    private var hasBackExitTimer = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusUtil.statusBar(window, false)
        super.onCreate(savedInstanceState)

        //设置viewBinding
        binding = DataBindingUtil.setContentView(this, layoutId)

        //设置viewModel
        try {
            viewModel = ViewModelProvider(this).get(getVmClass(this))
        } catch (e: Exception) {
        }

        intentParams(intent, savedInstanceState)

        bindView()

        bindData()

        initFailedViewModel()
    }

    private fun initFailedViewModel() {
        viewModel.failedEvent.observe(this, Observer {
            toast(it.message)
        })
    }

    open fun intentParams(intent: Intent, savedInstanceState: Bundle?) {

    }

    protected abstract fun bindView()

    protected abstract fun bindData()


    /**
     * 设置back键位 连按两次才可退出activity
     *
     * @param enable 设置back双击开关 true打开设置 false关闭设置
     */
    protected fun backExitEnable(enable: Boolean) {
        hasBackExit = enable
    }

    /**
     * 设置back键位 禁用开关
     * @param enable true打开禁用 false关闭禁用
     */
    protected fun banBackEnable(enable: Boolean) {
        hasBanBack = enable
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (hasBanBack) {
                return true
            }
            if (hasBackExit) {
                val secondTime = System.currentTimeMillis()
                //如果两次按键时间间隔大于2秒，则不退出
                if (secondTime - firstTime > hasBackExitTimer) {
                    Toast.makeText(this, getString(R.string.double_click_back), Toast.LENGTH_SHORT)
                        .show()
                    //更新firstTime
                    firstTime = secondTime
                    return true
                } else {//两次按键小于2秒时，退出应用
                    finish()
                }
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    @ExperimentalCoroutinesApi
    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    /**
     * 创建ViewModel
     */
    protected fun <T : ViewModel> createViewModel(cls: Class<T>): T {
        return ViewModelProviders.of(this).get(cls)
    }

    fun Activity.toast(message: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
        message ?: return
        Toast.makeText(this, message, duration).show()
    }

    fun AppCompatActivity.show(dialog: Dialog) {
        if (ActivityUtil.assertActivityDestroyed(this@BaseActivity)) return
        try {
            dialog.show()
        } catch (e: Exception) {
        }
    }

    fun AppCompatActivity.dismiss(dialog: Dialog) {
        if (ActivityUtil.assertActivityDestroyed(this@BaseActivity)) return
        try {
            dialog.dismiss()
        } catch (e: Exception) {
        }
    }

    fun AppCompatActivity.show(dialog: DialogFragment) {
        if (ActivityUtil.assertActivityDestroyed(this@BaseActivity)) return
        try {
            dialog.show(supportFragmentManager, dialog::class.java.simpleName)
        } catch (e: Exception) {
        }
    }

    fun AppCompatActivity.dismiss(dialog: DialogFragment) {
        if (ActivityUtil.assertActivityDestroyed(this@BaseActivity)) return
        try {
            dialog.dismiss()
        } catch (e: Exception) {
        }
    }

}