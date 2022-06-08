package com.lee.library.adapter.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * PageFragment适配器 -> 适用于viewPager
 * @author jv.lee
 */
class UiPagerAdapter : FragmentStatePagerAdapter {

    private var fragmentList: List<Fragment>
    private lateinit var tabList: List<String>

    constructor(fm: FragmentManager, fragmentList: List<Fragment>) :
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.fragmentList = fragmentList
    }

    constructor(
        fm: FragmentManager,
        fragmentList: List<Fragment>,
        tabList: List<String>
    ) : super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.fragmentList = fragmentList
        this.tabList = tabList
    }


    fun update(fragmentList: List<Fragment>, tabList: List<String>) {
        this.fragmentList = fragmentList
        this.tabList = tabList
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        if (!tabList.isNullOrEmpty() && tabList.size > position) {
            return tabList[position]
        }
        return ""
    }

    fun getFragmentList(): List<Fragment> {
        return fragmentList
    }

    fun setFragmentList(fragmentList: List<Fragment>) {
        this.fragmentList = fragmentList
    }

    fun getTabList(): List<String> {
        return tabList
    }

    fun setTabList(tabList: List<String>) {
        this.tabList = tabList
    }


}