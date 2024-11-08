package com.lee.ui

import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.activity.addCallback
import com.lee.library.adapter.core.UiPagerAdapter
import com.lee.library.base.BaseActivity
import com.lee.library.extensions.binding
import com.lee.library.tools.SystemBarTools.setDarkStatusIcon
import com.lee.library.tools.WindowViewHelper
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
            BannerFragment()
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
        window.setDarkStatusIcon()
        binding.vpContainer.adapter = vpAdapter

        binding.bottomNav.run {
            bindViewPager(binding.vpContainer)
            setDotVisibility(0, View.VISIBLE)
            setNumberDot(1, 999)
            setNumberDot(2, 7)
            postDelayed({ setNumberDot(2, 0) }, 2000)
            postDelayed({ setNumberDot(2, 17) }, 5000)
        }

        binding.floatingButton.setOnClickListener {
            startActivity(Intent(this, FloatingLayoutActivity::class.java))
        }
        binding.floatingGoogleButton.setOnClickListener {
            startActivity(Intent(this, NewGoogleViewActivity::class.java))
        }
        binding.floatingMotionButton.setOnClickListener {
            startActivity(Intent(this, MotionLayoutActivity::class.java))
        }
        binding.floatingWindowButton.setOnClickListener {
            createWindowAlertView()
        }

        onBackPressedDispatcher.addCallback {
            // 返回首页不finishActivity处理
            moveTaskToBack(true)
        }
    }

    override fun bindData() {
    }

    private fun createWindowAlertView() {
        val imageView = ImageView(this)
        imageView.setImageResource(R.mipmap.header)

        val windowViewHelper = WindowViewHelper.Builder()
            .setGravity(Gravity.START or Gravity.TOP)
            .setX(100)
            .setY(100)
            .build()

        windowViewHelper.showDelayHide(imageView)
    }
}
