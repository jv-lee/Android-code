package com.lee.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lee.library.adapter.core.UiPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val vpAdapter by lazy { UiPagerAdapter(supportFragmentManager, fragments, titles) }
    private val fragments by lazy { listOf(SelectorFragment(), WheelFragment(), ShadowFragment()) }
    private val titles by lazy { listOf(getString(R.string.nav_selector), getString(R.string.nav_wheel), getString(R.string.nav_shadow)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vp_container.adapter = vpAdapter
        vp_container.setNoScroll(true)

        bottom_nav.bindViewPager(vp_container)
        bottom_nav.setDotVisibility(0, View.VISIBLE)
        bottom_nav.setNumberDot(1, 999)
        bottom_nav.setNumberDot(2, 7)
        bottom_nav.run {
            postDelayed({ setNumberDot(2, 0) }, 2000)
            postDelayed({ setNumberDot(2, 17) }, 5000)
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

}
