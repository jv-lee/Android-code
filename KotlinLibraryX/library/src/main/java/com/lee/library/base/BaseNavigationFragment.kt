package com.lee.library.base

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.lee.library.R
import com.lee.library.extensions.getVmClass
import com.lee.library.mvvm.base.BaseViewModel

/**
 * @author jv.lee
 * @date 2020/3/30
 * @description
 */
abstract class BaseNavigationFragment(val layoutId: Int) :
    BaseFragment(layoutId) {

    private var isNavigationViewInit = false // 记录是否初始化view
    private var firstTime: Long = 0
    private var rootView: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        rootView = LayoutInflater.from(context).inflate(layoutId, null, false)
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {
        return super.createView(inflater, container)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!isNavigationViewInit) {
            navigationInit(view, savedInstanceState)
        }
    }

    open fun navigationInit(view: View, savedInstanceState: Bundle?) {
        intentParams(arguments, savedInstanceState)
        bindView()
        bindData()
        isNavigationViewInit = true
    }

    open fun setWebBackEvent(web: WebView) {
        web.isFocusable = true
        web.isFocusableInTouchMode = true
        web.requestFocus()
        web.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(view: View?, i: Int, keyEvent: KeyEvent?): Boolean {
                if (web.canGoBack()) {
                    web.goBack()
                    return true
                }
                return false
            }

        })
    }

    /**
     * 设置back键位 连按两次才可退出activity
     */
    fun backDoubleClick() {
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val secondTime = System.currentTimeMillis()
                    //如果两次按键时间间隔大于2秒，则不退出
                    if (secondTime - firstTime > 2000) {
                        getString(R.string.double_click_back)
                        //更新firstTime
                        firstTime = secondTime
                    } else {//两次按键小于2秒时，退出应用
                        requireActivity().finish()
                    }
                }
            })
    }

}