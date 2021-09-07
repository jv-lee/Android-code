package com.lee.library.adapter.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author jv.lee
 * description：PageFragment适配器 -> 适用于ViewPager2
 */
class UiPager2Adapter : FragmentStateAdapter {

    private val fragmentList: List<Fragment>

    constructor(fragmentActivity: FragmentActivity, fragments: List<Fragment>)
            : super(fragmentActivity) {
        fragmentList = fragments
    }

    constructor(fragment: Fragment, fragments: List<Fragment>) : super(fragment) {
        fragmentList = fragments
    }

    override fun createFragment(position: Int) = fragmentList[position]

    override fun getItemCount() = fragmentList.size

    fun getFragments() = fragmentList
}