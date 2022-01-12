package com.lee.library.adapter.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author jv.lee
 * description：PageFragment适配器 -> 适用于ViewPager2
 */
class UiPagerAdapter2 : FragmentStateAdapter {
    private val fragmentList: MutableList<Fragment>

    constructor(fragmentActivity: FragmentActivity, fragments: MutableList<Fragment>)
            : super(fragmentActivity) {
        fragmentList = fragments
    }

    constructor(fragment: Fragment, fragments: MutableList<Fragment>) : super(fragment) {
        fragmentList = fragments
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    fun getFragments() = fragmentList

}