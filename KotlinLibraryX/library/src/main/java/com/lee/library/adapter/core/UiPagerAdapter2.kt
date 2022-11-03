package com.lee.library.adapter.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * PageFragment适配器 -> 适用于ViewPager2
 * @author jv.lee
 */
class UiPagerAdapter2(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentList = ArrayList<Fragment>()

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    fun getFragments() = fragmentList

    fun addAll(fragments: MutableList<Fragment>) {
        clear()
        fragmentList.addAll(fragments)
    }

    fun clear() {
        fragmentList.clear()
    }
}