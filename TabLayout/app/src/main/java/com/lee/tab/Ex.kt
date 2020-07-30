package com.lee.tab

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

/**
 * @author jv.lee
 * @date 2020/7/30
 * @description
 */

fun TabLayout.bindTab(
    tabList: ArrayList<String>,
    getTabViewBlock: (index: Int) -> View,
    updateTabTextView: (tab: TabLayout.Tab, isSelect: Boolean) -> Unit
) {
    addTab(newTab().setText(tabList[0]))
    addTab(newTab().setText(tabList[1]))

    for (i in 0 until tabCount) {
        val tab = getTabAt(i)
        if (tab != null) {
            tab.customView = getTabViewBlock(i)
        }
    }

    getTabAt(selectedTabPosition)?.let { updateTabTextView(it, true) }

    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            updateTabTextView(tab, true)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            updateTabTextView(tab, false)
        }

        override fun onTabReselected(tab: TabLayout.Tab) {}
    })

}

fun TabLayout.bindViewPager(viewPager: ViewPager) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.run {
                viewPager.setCurrentItem(position, true)
            }
        }
    })
    viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            selectTab(getTabAt(position))
        }
    })
}