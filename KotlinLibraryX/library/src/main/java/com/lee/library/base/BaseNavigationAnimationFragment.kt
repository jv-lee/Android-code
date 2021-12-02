package com.lee.library.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils

/**
 * @author jv.lee
 * @date 2020/3/30
 * @description
 */
abstract class BaseNavigationAnimationFragment(val layoutId: Int) :
    BaseFragment(layoutId), Animation.AnimationListener {

    private var isViewInit = false
    private var isDataInit = false
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
        initView()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (nextAnim == 0) {
            initData()
            return super.onCreateAnimation(transit, enter, nextAnim)
        }

        val animation = AnimationUtils.loadAnimation(requireContext(), nextAnim)
        animation.setAnimationListener(this)
        return animation
    }

    override fun onAnimationRepeat(animation: Animation?) {
    }

    override fun onAnimationStart(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
        initData()
    }

    private fun initView() {
        if (!isViewInit) {
            bindView()
            isViewInit = true
        }
    }

    private fun initData() {
        if (!isDataInit) {
            bindData()
            isDataInit = true
        }
    }

}