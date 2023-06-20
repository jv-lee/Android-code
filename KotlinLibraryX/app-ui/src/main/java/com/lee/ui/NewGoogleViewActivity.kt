package com.lee.ui

import android.annotation.SuppressLint
import android.graphics.Color
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.tabs.TabLayout
import com.lee.library.base.BaseActivity
import com.lee.library.extensions.binding
import com.lee.ui.databinding.ActivityNewGoogleViewBinding

/**
 * google官方 最新view api展示
 * @author jv.lee
 * @date 2022/1/13
 */
class NewGoogleViewActivity : BaseActivity() {

    private val binding by binding(ActivityNewGoogleViewBinding::inflate)

    override fun bindView() {
        binding.toolbar.setNavigationOnClickListener { finish() }

        initTabLayout()
        initBadgeView()
    }

    override fun bindData() {
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initTabLayout() {
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initBadgeView() {
        // tabLayout设置红点
        binding.tabLayout.getTabAt(0)?.let {
            it.orCreateBadge.apply {
                backgroundColor = Color.RED
                badgeTextColor = Color.WHITE
                number = 6
            }
        }
        binding.tabLayout.getTabAt(1)?.let {
            it.orCreateBadge.apply {
                backgroundColor = Color.RED
                badgeTextColor = Color.WHITE
            }
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.removeBadge()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        // 文本设置红点
        binding.tvBadge.post {
            val badge = BadgeDrawable.create(this).apply {
                badgeGravity = BadgeDrawable.TOP_END
                number = 6
                backgroundColor = Color.RED
                isVisible = true
                BadgeUtils.attachBadgeDrawable(this, binding.tvBadge)
            }
            binding.tvBadge.setOnClickListener {
                BadgeUtils.detachBadgeDrawable(badge, it)
            }
        }

        // 按钮设置红点
        binding.buttonBadge.post {
            val badge = BadgeDrawable.create(this).apply {
                badgeGravity = BadgeDrawable.TOP_START
                number = 6
                backgroundColor = Color.RED
                verticalOffset = 15
                horizontalOffset = 10
                isVisible = true
                BadgeUtils.attachBadgeDrawable(this, binding.buttonBadge)
            }
            binding.buttonBadge.setOnClickListener {
                BadgeUtils.detachBadgeDrawable(badge, it)
            }
        }

        // 图片设置红点
        binding.ivBadge.post {
            val badge = BadgeDrawable.create(this).apply {
                badgeGravity = BadgeDrawable.TOP_END
                number = 6
                backgroundColor = Color.RED
                isVisible = true
                BadgeUtils.attachBadgeDrawable(this, binding.ivBadge, binding.frameBadge)
            }
            binding.ivBadge.setOnClickListener {
                BadgeUtils.detachBadgeDrawable(badge, it)
            }
        }

        // 导航栏设置红点 (999+ 在5个导航栏会有显示bug)
        binding.bottomNavigation.getOrCreateBadge(R.id.shimmer).apply {
            backgroundColor = Color.RED
            badgeTextColor = Color.WHITE
            number = 9999
        }
        binding.bottomNavigation.setOnItemSelectedListener {
            binding.bottomNavigation.getBadge(it.itemId)?.isVisible = false
            true
        }
    }
}