package com.gionee.gnservice.view.activity

import android.os.Bundle
import android.view.MenuItem
import com.gionee.gnservice.app.R
import com.gionee.gnservice.view.fragment.*
import com.lee.library.adapter.UiPagerAdapter
import com.lee.library.base.BaseActivity
import com.lee.library.ioc.annotation.ContentView
import com.lee.library.utils.StatusUtil
import com.lee.library.widget.nav.BottomNavView
import kotlinx.android.synthetic.main.activity_main.*

@ContentView(R.layout.activity_main)
class MainActivity : BaseActivity(), BottomNavView.ItemPositionListener {

    private lateinit var vpAdapter: UiPagerAdapter
    private val fragments = arrayOf(NewsFragment(), VideoFragment(), NovelFragment(), GameFragment(), WalletFragment())

    override fun bindData(savedInstanceState: Bundle?) {
        StatusUtil.setStatusFontLight2(mActivity)
        vpAdapter = UiPagerAdapter(supportFragmentManager, fragments)
    }

    override fun bindView() {
        vp_container.adapter = vpAdapter
        vp_container.setNoScroll(true)
        vp_container.offscreenPageLimit = fragments.size - 1

        nav.itemIconTintList = null
        nav.bindViewPager(vp_container)
        nav.setItemPositionListener(this@MainActivity)
    }

    /**
     * 导航tab选中监听
     */
    override fun onPosition(menuItem: MenuItem?, position: Int) {

    }

}
