package com.lee.library.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
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
abstract class BaseActivity<V : ViewDataBinding, VM : ViewModel>(
    var layoutId: Int,
    var vm: Class<VM>?
) :
    AppCompatActivity(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    protected lateinit var mActivity: AppCompatActivity

    private var firstTime: Long = 0
    private var hasBackExit = false
    private var hasBackExitTimer = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        StatusUtil.statusBar(this, false)
        super.onCreate(savedInstanceState)
        mActivity = this

        //设置viewBinding
        binding = DataBindingUtil.setContentView(this, layoutId)

        //设置viewModel
        if (vm != null) viewModel = ViewModelProviders.of(this).get<VM>(vm!!)

        intentParams(intent, savedInstanceState)

        bindView()

        bindData()
    }

    open fun intentParams(intent: Intent, savedInstanceState: Bundle?) {

    }

    protected abstract fun bindView()

    protected abstract fun bindData()


    /**
     * 设置back键位 连按两次才可退出activity
     *
     * @param enable 设置back双击开关
     */
    protected fun backExitEnable(enable: Boolean) {
        hasBackExit = enable
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (hasBackExit) {
                val secondTime = System.currentTimeMillis()
                //如果两次按键时间间隔大于2秒，则不退出
                if (secondTime - firstTime > hasBackExitTimer) {
                    Toast.makeText(this, "再次按下退出", Toast.LENGTH_SHORT).show()
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

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(mActivity, message, duration).show()
    }

}