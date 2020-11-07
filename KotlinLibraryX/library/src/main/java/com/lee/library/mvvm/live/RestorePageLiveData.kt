package com.lee.library.mvvm.live

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager

/**
 * @author jv.lee
 * @date 2020/10/22
 * @description 帮助页面重构后 恢复viewPager选项
 */
class RestorePageLiveData : MutableLiveData<Int>() {

    fun bindPager(lifecycleOwner: LifecycleOwner, viewPager: ViewPager) {
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
                if (position != 0) value = position
            }

        })
        observe(lifecycleOwner, Observer {
            if (viewPager.currentItem != it && viewPager.childCount > it) {
                viewPager.currentItem = it
            }
        })
    }
}