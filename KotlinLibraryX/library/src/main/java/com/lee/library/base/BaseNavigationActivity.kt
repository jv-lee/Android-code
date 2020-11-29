package com.lee.library.base

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lee.library.mvvm.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * @author jv.lee
 * @date 2019-08-15
 * @description
 */
abstract class BaseNavigationActivity<V : ViewDataBinding, VM : BaseViewModel>(layoutId: Int) :
    BaseActivity<V, VM>(layoutId), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private var navVisible = false

    private var bottomNavigationView: BottomNavigationView? = null
    private var fragmentContainerView: FragmentContainerView? = null

    public fun hideView() {
        navVisible = true
        bottomNavigationView?.visibility = View.GONE
    }

    public fun showView() {
        if (navVisible) {
            bottomNavigationView?.visibility = View.VISIBLE
            fragmentContainerView?.visibility = View.VISIBLE
        }
    }

    protected fun setNavigationVisible(visible: Boolean) {
        this.navVisible = visible
    }

    protected fun withBottomNavigationView(bottomNavigationView: BottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView
    }

    protected fun withFragmentContainerView(fragmentContainerView: FragmentContainerView) {
        this.fragmentContainerView = fragmentContainerView
    }

}