package com.lee.ui

import android.content.Intent
import android.view.View
import com.lee.library.adapter.core.UiPagerAdapter
import com.lee.library.base.BaseActivity
import com.lee.library.extensions.binding
import com.lee.ui.databinding.ActivityMainBinding
import com.lee.ui.fragment.*


class MainActivity : BaseActivity() {

    private val binding by binding(ActivityMainBinding::inflate)

    private val vpAdapter by lazy { UiPagerAdapter(supportFragmentManager, fragments, titles) }
    private val fragments by lazy {
        listOf(
            SelectorFragment(),
            WheelFragment(),
            ShadowFragment(),
            ShimmerFragment(),
            BannerFragment(),
        )
    }
    private val titles by lazy {
        listOf(
            getString(R.string.nav_selector),
            getString(R.string.nav_wheel),
            getString(R.string.nav_shadow),
            getString(R.string.nav_shimmer),
            getString(R.string.nav_banner)
        )
    }

    override fun bindView() {
        binding.vpContainer.adapter = vpAdapter
        binding.vpContainer.setNoScroll(true)

        binding.bottomNav.run {
            bindViewPager(binding.vpContainer)
            setDotVisibility(0, View.VISIBLE)
            setNumberDot(1, 999)
            setNumberDot(2, 7)
            postDelayed({ setNumberDot(2, 0) }, 2000)
            postDelayed({ setNumberDot(2, 17) }, 5000)
        }

        binding.floatingButton.setOnClickListener {
            startActivity(Intent(this, NewGoogleViewActivity::class.java))
        }

    }

    override fun bindData() {

    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

}
