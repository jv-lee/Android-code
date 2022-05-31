package com.lee.library.widget.viewpager

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.lee.library.widget.viewpager.WrappingViewPager

/**
 *
 * @author jv.lee
 * @date 2020/11/2
 */
abstract class WrappingPagerAdapter : PagerAdapter() {

    private var mCurrentPosition = -1

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)

        if (container is WrappingViewPager) {
            if (`object` is View) {
                val view: View = `object`
                if (position != mCurrentPosition) {
                    mCurrentPosition = position
                }
                container.onPageChanged(view)
            }
        }

    }
}