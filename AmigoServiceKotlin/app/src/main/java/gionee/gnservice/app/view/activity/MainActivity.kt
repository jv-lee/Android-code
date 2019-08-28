package gionee.gnservice.app.view.activity

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.os.Debug
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.gionee.gnservice.config.AppConfig
import com.gionee.gnservice.module.setting.push.PushHelper
import com.gionee.gnservice.module.setting.push.PushIntentUtils
import com.gionee.gnservice.module.setting.push.PushReceiver
import com.gionee.gnservice.statistics.StatisticsUtil
import com.gionee.gnservice.utils.SdkUtil
import com.lee.library.adapter.UiPagerAdapter
import com.lee.library.base.BaseActivity
import com.lee.library.utils.StatusUtil
import com.lee.library.widget.nav.BottomNavView
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.databinding.ActivityMainBinding
import gionee.gnservice.app.view.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
    BaseActivity<ActivityMainBinding, ViewModel>(R.layout.activity_main, null),
    BottomNavView.ItemPositionListener {

    private val fragments by lazy {
        arrayOf(NewsFragment(), VideoFragment(), NovelFragment(), GameFragment(), WalletFragment())
    }

    override fun bindData(savedInstanceState: Bundle?) {
        StatusUtil.setStatusFontLight2(mActivity)
        StatisticsUtil.onEvent(this, EventConstants.Exposure_UCenterPage, "用户中心")

        initEvent()
        initPush()
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

    /**
     * 初始化统计
     */
    private fun initEvent() {
        val notification = intent.getIntExtra(Constants.NOTIFICATION_LABLE, 0)
        if (notification == 1) {
            StatisticsUtil.onEvent(this, EventConstants.Client_Notification, "点击")
        }

        var lable = "桌面"
        if (intent.getStringExtra("k1") != null) {
            lable = "推送"
        } else if (intent.data?.getQueryParameter("ch") != null) {
            lable = intent.data?.getQueryParameter("ch")!!
        }
        StatisticsUtil.onEvent(mActivity, EventConstants.Cold_Start, lable)
    }

    private fun initPush() {
        val k1 = intent.getStringExtra("k1")
        val vtype = intent.getIntExtra("vtype", 0)
        //通过用户中心推送消息打开
        if (vtype != 0) {
            startTabMode(vtype)
        }
        //通过系统推送打开
        if (k1 != null) {
            val entity = PushIntentUtils.getEntity(k1)
            startTabMode(entity.data.vtype)
        }
        //增加统计
        PushReceiver.startSaveServer(mActivity, intent.getStringExtra(PushReceiver.REGISTERATION_KEY_MESSAGE_ID))
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

}
