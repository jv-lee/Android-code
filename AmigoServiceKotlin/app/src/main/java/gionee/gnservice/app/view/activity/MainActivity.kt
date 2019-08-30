package gionee.gnservice.app.view.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import com.gionee.gnservice.module.setting.push.PushHelper
import com.gionee.gnservice.statistics.StatisticsUtil
import com.lee.library.adapter.UiPagerAdapter
import com.lee.library.base.BaseActivity
import com.lee.library.utils.StatusUtil
import com.lee.library.widget.nav.BottomNavView
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.databinding.ActivityMainBinding
import gionee.gnservice.app.model.entity.Magnet
import gionee.gnservice.app.tool.GlideTool
import gionee.gnservice.app.tool.PrefAccess
import gionee.gnservice.app.tool.ToastTool
import gionee.gnservice.app.view.fragment.*
import gionee.gnservice.app.vm.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main, MainViewModel::class.java),
    BottomNavView.ItemPositionListener {

    private val fragments by lazy {
        arrayOf(NewsFragment(), VideoFragment(), NovelFragment(), GameFragment(), WalletFragment())
    }

    override fun bindData(savedInstanceState: Bundle?) {
        StatusUtil.setStatusFontLight2(mActivity)
        PushHelper.registerPushRid(this)

        viewModel.apply {
            //推送奖励
            push.observe(this@MainActivity, Observer {
                if (it?.haveAward == 1) {
                    ToastTool.show(this@MainActivity, "推送点击奖励 +${it.count}")
                }
            })
            //推送跳转
            type.observe(this@MainActivity, Observer {
                startTabMode(it!!)
            })
            //红点提示
            redPoint.observe(this@MainActivity, Observer {
                if (it != null) {
                    binding.nav.setDotNotRead(4, it.t1)
                }
            })
            //磁铁or活动
            magnet.observe(this@MainActivity, Observer { it ->
                if (it == null) return@Observer
                if (isDestroyed) return@Observer
                if (it.list.first_login == 1) {
//                    showActive(it.list.skipurl)
                }
                if (it.list.isActive == 1) {
                    GlideTool.loadImage(it.list.icon, binding.ivActive)
                    binding.ivActive.setOnClickListener { v ->
                        StatisticsUtil.onEvent(this@MainActivity, "", "")
                        showActiveMode(it)
                    }
                }
            })
        }

        //初始化配置及事件统计
        viewModel.getConfig()
        viewModel.initEvent(intent)
        viewModel.initPush(intent)
        viewModel.initRedPoint()

        initUpdateOrActive()
    }

    override fun bindView() {
        binding.vpContainer.adapter = UiPagerAdapter(supportFragmentManager, fragments)
        binding.vpContainer.setNoScroll(true)
        binding.vpContainer.offscreenPageLimit = fragments.size - 1

        binding.nav.itemIconTintList = null
        binding.nav.bindViewPager(binding.vpContainer)
        binding.nav.setItemPositionListener(this@MainActivity)
    }

    /**
     * 导航tab选中监听
     */
    override fun onPosition(menuItem: MenuItem?, position: Int) {
        when (position) {
            0 -> {

            }
            1 -> {

            }
            2 -> {

            }
            3 -> {

            }
            4 -> {

            }
        }
    }

    private fun startTabMode(code: Int) {
        when (code) {
            //小说
            1 -> nav.toPosition(2)
            //游戏
            2 -> nav.toPosition(3)
            //资讯
            3 -> nav.toPosition(0)
            //钱包
            4 -> nav.toPosition(4)
            //钱包任务栏
            5 -> nav.toPosition(4)
        }
    }

    private fun startTabMode2(code: Int) {
        when (code) {
            Constants.MAGNET_NEWS -> nav.toPosition(0)
            Constants.MAGNET_VIDEO -> nav.toPosition(1)
            Constants.MAGNET_NOVEL -> nav.toPosition(2)
            Constants.MAGNET_GAME -> nav.toPosition(3)
            Constants.MAGNET_WALLET -> nav.toPosition(4)
            Constants.MAGNET_WALLET_TASK -> {
            }
            Constants.MAGNET_WECHAT -> {
            }
        }
    }

    private fun showActiveMode(entity: Magnet) {
        when (entity.list.way) {
            //内部页面跳转
            Constants.MAGNET_TYPE_TAB -> startTabMode2(entity.list.skip)
            //H5游戏跳转
            Constants.MAGNET_TYPE_GAME -> startActivity(
                Intent(this, GameActivity::class.java)
                    .putExtra(Constants.URL, entity.list.game.linkurl)
                    .putExtra(Constants.V_TYPE, entity.list.game.landspace)
            )
            //活动页面跳转
//            Constants.MAGNET_TYPE_ACTIVE -> showActive(entity.list.skipurl)
            Constants.MAGNET_TYPE_SDK_H5 -> startActivity(
                Intent(this, WebActivity::class.java)
                    .putExtra(Constants.URL, entity.list.skipurl)
                    .putExtra(Constants.V_TYPE, 1)
                    .putExtra(Constants.TITLE, entity.list.name)
            )
        }
    }

    private fun initUpdateOrActive() {
        if (PrefAccess.isOpenUpdate()) {
            if (!isDestroyed) {

            }
        } else {
            if (!isDestroyed) {

            }
        }
    }

//    private fun showActive(url: String) {
//        if (activeFragment == null) {
//            activeFragment = ActiveDialogFragment.getInstance(url)
//        }
//        if (activeFragment.isAdded()) {
//            supportFragmentManager.beginTransaction().remove(activeFragment).commit()
//        }
//        activeFragment.show(supportFragmentManager, activeFragment.getClass().getSimpleName())
//    }
//
//    private fun showUpdate() {
//        if (updateDialogFragment == null) {
//            updateDialogFragment = UpdateDialogFragment.getInstance()
//        }
//        if (updateDialogFragment.isAdded()) {
//            supportFragmentManager.beginTransaction().remove(updateDialogFragment).commit()
//        }
//        updateDialogFragment.show(supportFragmentManager, updateDialogFragment.getClass().getSimpleName())
//    }
//
//
//    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            //判断用户中心fragment是否显示 处理相应操作
//            if (usercenterContainer != null && usercenterContainer.getVisibility() != View.INVISIBLE) {
//                showOrHide(true)
//                usercenterContainer.setVisibility(View.INVISIBLE)
//                return true
//            }
//            //非新闻tab 先跳回news tab
//            if (fragments[binding.vpContainer.currentItem] !is NewsFragment) {
//                nav.toPosition(0)
//                return true
//            }
//
//            //显示退出提示弹窗
//            if (fragment == null) {
//                fragment = BackDialogFragment()
//            }
//            if (fragment.isAdded()) {
//                supportFragmentManager.beginTransaction().remove(fragment).commit()
//            }
//            fragment.show(supportFragmentManager, fragment.getClass().getSimpleName())
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }

}
