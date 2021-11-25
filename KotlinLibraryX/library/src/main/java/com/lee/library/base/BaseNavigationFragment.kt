package com.lee.library.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.OneShotPreDrawListener

/**
 * @author jv.lee
 * @date 2020/3/30
 * @description
 */
abstract class BaseNavigationFragment(val layoutId: Int) :
    BaseFragment(layoutId) {

    private var isNavigationViewInit = false // 记录是否初始化view
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
        postponeEnterTransition()
        bindView()
        bindData()
        isNavigationViewInit = true
        (view.parent as? ViewGroup)?.apply {
            OneShotPreDrawListener.add(this) {
                startPostponedEnterTransition()
            }
        }
    }

}