package gionee.gnservice.app.view.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import com.gionee.gnservice.statistics.StatisticsUtil
import com.gionee.simple.UserCenterFragment
import com.lee.library.adapter.UiPagerAdapter
import com.lee.library.base.BaseActivity
import com.lee.library.livedatabus.InjectBus
import com.lee.library.livedatabus.LiveDataBus
import com.lee.library.utils.FragmentUtil
import com.lee.library.utils.LogUtil
import com.lee.library.utils.StatusUtil
import com.lee.library.widget.nav.BottomNavView
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.Cache
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.constants.StatisticsConstants
import gionee.gnservice.app.databinding.ActivityMainBinding
import gionee.gnservice.app.model.entity.Magnet
import gionee.gnservice.app.model.server.RetrofitUtils
import gionee.gnservice.app.tool.*
import gionee.gnservice.app.view.fragment.*
import gionee.gnservice.app.view.widget.BlissBagView
import gionee.gnservice.app.view.widget.window.FloatWindowManager
import gionee.gnservice.app.view.widget.window.FloatWindowView
import gionee.gnservice.app.view.widget.window.WindowCallback
import gionee.gnservice.app.vm.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description 主页面 （包含主TAB 新闻/视频/小说/游戏/钱包）
 */
class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main, MainViewModel::class.java),
    BottomNavView.ItemPositionListener {

    private var userCenterFragment: UserCenterFragment? = null
    private val fragments by lazy {
        arrayOf(NewsFragment(), VideoFragment(), NovelFragment(), GameFragment(), WalletFragment())
    }

    private var floatWindowView: FloatWindowView? = null
    private var blissBagView: BlissBagView? = null

    /**
     * 通过Handler轮询 红包雨活动
     */
    private var handler: Handler? = Handler(Handler.Callback { false })
    private var runnable = Runnable { runnableFun() }
    private fun runnableFun() {
        if (Cache.isPlayGame || Cache.isPlayVideo) {
            handler?.postDelayed(runnable, 1000 * 60)
        } else {
            startActivity(Intent(this, RedPackageActivity::class.java))
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
        }
    }

    override fun bindData(savedInstanceState: Bundle?) {
        StatusUtil.setStatusFontLight2(mActivity)
        LiveDataBus.getInstance().injectBus(this)
        AManager.getInstance().init(this)

        //添加隐藏的用户中心fragment 初始化用户中心基础组件
        createUserCenterFragment()

        viewModel.apply {
            //TODO 推送奖励
            push.observe(this@MainActivity, Observer {
                if (it?.haveAward == 1) ToastTool.show(
                    this@MainActivity,
                    getString(R.string.notification_awared, it.count.toString())
                )
            })

            //TODO 推送跳转
            type.observe(this@MainActivity, Observer {
                startTabMode(CommonTool.pushTransformCode(it!!))
            })

            //TODO 红点提示
            redPoint.observe(this@MainActivity, Observer {
                if (it != null && it.t1 > 0) binding.nav.setDotVisibility(4, View.VISIBLE)
            })

            //TODO 磁铁or活动
            magnet.observe(this@MainActivity, Observer {
                if (it == null) return@Observer
                if (isDestroyed) return@Observer
                if (it.list.first_login == 1) {
                    FragmentUtil.getInstance().showFragmentDialog(
                        this@MainActivity,
                        null,
                        DialogActiveFragment.get(it.list.skipurl)
                    )
                }
                //加载磁贴图及设置磁贴行为
                if (it.list.isActive == 1) {
                    GlideTool.loadImage(it.list.icon, binding.ivActive)
                    binding.ivActive.setOnClickListener { v ->
                        StatisticsUtil.onEvent(this@MainActivity, StatisticsConstants.MAGNET_CLICK)
                        showActiveMode(it)
                    }
                }
            })

            //TODO 初始化 跳转tab
            tabIndex.observe(this@MainActivity, Observer {
                if (it == null) {
                    return@Observer
                }
                if (it.menu.index == 0) {
                    binding.ivActive.visibility = View.VISIBLE
                } else {
                    binding.ivActive.visibility = View.INVISIBLE
                }
                nav.toPosition(it.menu.index)
            })

            //TODO 新手奖励提示窗
            novice.observe(this@MainActivity, Observer {
                if (it == null) return@Observer
                if (it.isOverOpenClick == 1) {
                    FragmentUtil.getInstance().showFragmentDialog(this@MainActivity, null, DialogNoviceFragment())
                }
            })
        }

        //初始化配置及事件统计,及磁贴
        viewModel.getConfig()
        viewModel.initEvent(intent)
        viewModel.initPush(intent)
        viewModel.initMagnetActive()
        viewModel.initRedPoint()

        initUpdateAlert()
        initFloatWindow()
    }

    override fun bindView() {
        binding.vpContainer.adapter = UiPagerAdapter(supportFragmentManager, fragments)
        binding.vpContainer.setNoScroll(true)
        binding.vpContainer.offscreenPageLimit = fragments.size - 1

        binding.nav.itemIconTintList = null
        binding.nav.bindViewPager(binding.vpContainer)
        binding.nav.setItemPositionListener(this@MainActivity)

        //设置引导红点
        PrefAccess.isGuide(0, binding.nav, PrefAccess.KEY_GUIDE_FINISHED_NEWS)
        PrefAccess.isGuide(0, binding.nav, PrefAccess.KEY_GUIDE_FINISHED_VIDEO)
        PrefAccess.isGuide(0, binding.nav, PrefAccess.KEY_GUIDE_FINISHED_NOVEL)

        viewModel.initTabIndex(intent)
    }

    /**
     * TODO 导航tab选中监听
     */
    override fun onPosition(menuItem: MenuItem?, position: Int) {
        when (position) {
            0 -> {
                LogUtil.i("nav -> news")
                StatisticsUtil.onEvent(
                    applicationContext,
                    StatisticsConstants.USER_TAGSCLICK,
                    StatisticsConstants.Label_TAB_NEWS
                )
                PrefAccess.useGuide(0, binding.nav, PrefAccess.KEY_GUIDE_FINISHED_NEWS)
            }
            1 -> {
                LogUtil.i("nav -> video")
                StatisticsUtil.onEvent(
                    applicationContext,
                    StatisticsConstants.USER_TAGSCLICK,
                    StatisticsConstants.Label_TAB_VIDEO
                )
                PrefAccess.useGuide(1, binding.nav, PrefAccess.KEY_GUIDE_FINISHED_VIDEO)
            }
            2 -> {
                LogUtil.i("nav -> novel")
                StatisticsUtil.onEvent(
                    applicationContext,
                    StatisticsConstants.USER_TAGSCLICK,
                    StatisticsConstants.Label_TAB_NOVEL
                )
                PrefAccess.useGuide(2, binding.nav, PrefAccess.KEY_GUIDE_FINISHED_NOVEL)
            }
            3 -> {
                LogUtil.i("nav -> game")
                StatisticsUtil.onEvent(
                    applicationContext,
                    StatisticsConstants.USER_TAGSCLICK,
                    StatisticsConstants.Label_TAB_GAME
                )
            }
            4 -> {
                LogUtil.i("nav -> wallet")
                StatisticsUtil.onEvent(
                    applicationContext,
                    StatisticsConstants.USER_TAGSCLICK,
                    StatisticsConstants.Label_TAB_WALLET
                )
            }
        }
    }

    /**
     * TODO 刷新红点提示事件
     */
    @InjectBus(EventConstants.UPDATE_RED_POINT)
    fun notificationPoint(code: Int) {
        viewModel.initRedPoint()
    }

    /**
     * TODO 更新福袋显示
     */
    @InjectBus(EventConstants.NOTIFICATION_AWARD)
    fun notificationAward(count: Int) {
        if (count == 0) return
        if (count > Cache.ydCount) {
            AudioUtil.playMusic(this, R.raw.award)
            blissBagView?.notificationAward(count - Cache.ydCount)
        } else {
            blissBagView?.setCurrentAward(count)
        }
        Cache.ydCount = count
    }

    /**
     * TODO 页面跳转
     */
    @InjectBus(EventConstants.START_PAGE)
    fun startTabMode(code: Int) {
        when (code) {
            Constants.TAB_NEWS -> nav.toPosition(0)
            Constants.TAB_VIDEO -> nav.toPosition(1)
            Constants.TAB_NOVEL -> nav.toPosition(2)
            Constants.TAB_GAME -> nav.toPosition(3)
            Constants.TAB_WALLET -> nav.toPosition(4)
            Constants.TAB_WALLET_TASK -> {
                nav.toPosition(4)
                LiveDataBus.getInstance().getChannel(EventConstants.START_WALLET_MODE).value = 1
            }
            Constants.TAB_WALLET_WECHAT -> {
                nav.toPosition(4)
                LiveDataBus.getInstance().getChannel(EventConstants.START_WALLET_MODE).value = 0
            }
        }
    }

    /**
     * TODO 启动红包雨事件
     */
    @InjectBus(EventConstants.RED_PACKAGE_ACTIVITY)
    fun showRedPackageActivity(code: Int) {
        if (Cache.nextDT != 0) {
            handler?.removeCallbacks(runnable)
            handler?.postDelayed(runnable, (Cache.nextDT * 1000).toLong())
        }
    }

    /**
     * TODO 首次打开奖励
     */
    @InjectBus(EventConstants.NOVICE_AWARD)
    fun showNoviceAward(code: Int) {
        if (PrefAccess.isFirstOpen()) viewModel.initNoviceAward()
    }

    /**
     * TODO 活动跳转方式
     */
    private fun showActiveMode(entity: Magnet) {
        when (entity.list.way) {
            //内部页面跳转
            Constants.MAGNET_TYPE_TAB -> startTabMode(CommonTool.activeTransformCode(entity.list.skip))
            //H5游戏跳转
            Constants.MAGNET_TYPE_GAME -> startActivity(
                Intent(this, GameActivity::class.java)
                    .putExtra(Constants.URL, entity.list.game.linkurl)
                    .putExtra(Constants.V_TYPE, entity.list.game.landspace)
            )
            //活动页面跳转
            Constants.MAGNET_TYPE_ACTIVE -> {
                FragmentUtil.getInstance()
                    .showFragmentDialog(this@MainActivity, null, DialogActiveFragment.get(entity.list.skipurl))
            }
            //H5页面跳转
            Constants.MAGNET_TYPE_SDK_H5 -> startActivity(
                Intent(this, WebActivity::class.java)
                    .putExtra(Constants.URL, entity.list.skipurl)
                    .putExtra(Constants.V_TYPE, 1)
                    .putExtra(Constants.TITLE, entity.list.name)
            )
        }
    }

    /**
     * TODO 初始化用户中心碎片
     */
    private fun createUserCenterFragment() {
        if (userCenterFragment == null) userCenterFragment = UserCenterFragment()
        supportFragmentManager.beginTransaction().add(R.id.frame_usercenter_container, userCenterFragment!!).commit()
    }

    /**
     * TODO 提供给钱包界面获取用户中心碎片
     */
    fun getUserCenterFragment(): UserCenterFragment {
        return userCenterFragment!!
    }

    fun showUserCenter() {
        binding.nav.visibility = View.INVISIBLE
        binding.frameUsercenterContainer.visibility = View.VISIBLE
    }

    /**
     * TODO 初始化更新提示 或 红包雨和磁贴
     */
    private fun initUpdateAlert() {
        if (PrefAccess.isOpenUpdate()) {
            //显示更新dialog
            FragmentUtil.getInstance().showFragmentDialog(this, null, DialogUpdateFragment())
        } else {
            //显示红包雨
            DelayTimeTool.timeChange(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    initRedPackage()
                }
            }, 5000, 0, 1)
        }
    }

    /**
     * TODO 初始化红包雨 外部调用 DialogUpdateFragment
     */
    fun initRedPackage() {
        val user = RetrofitUtils.instance.getUser()
        if ((user?.redPacketInfo?.isOpenLogin == 1 || user?.redPacketInfo?.isOpenTime == 1) && user.isNew == 0) {
            startActivity(Intent(this, RedPackageActivity::class.java))
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
        } else {
            LiveDataBus.getInstance().getChannel(EventConstants.RED_PACKAGE_ACTIVITY).value = 0
            LiveDataBus.getInstance().getChannel(EventConstants.NOVICE_AWARD).value = 0
        }
    }

    /**
     * TODO 初始化全局福袋窗口
     */
    private fun initFloatWindow() {
        FloatWindowManager()
            .requestPermission(this@MainActivity, object : WindowCallback {
                override fun success() {
                    StatisticsUtil.onEvent(
                        applicationContext,
                        StatisticsConstants.Float_Icon_Permission,
                        StatisticsConstants.Label_Permission_Success
                    )
                    blissBagView = BlissBagView(applicationContext)
                    blissBagView?.setCurrentAward(0)

                    try {
                        floatWindowView = FloatWindowView(applicationContext)
                        floatWindowView?.bindView(blissBagView)
                        //设置可以显示的activity
                        floatWindowView?.bindActivity(
                            arrayOf(
                                MainActivity::class.java,
                                VideoDetailsActivity::class.java,
                                Class.forName("com.hs.feed.ui.activity.WebViewDetailActivity")
                            )
                        )
                        //设置点击跳转意图
                        floatWindowView?.setOnClickListener {
                            if (blissBagView?.isHasJump!!) {
                                StatisticsUtil.onEvent(applicationContext, StatisticsConstants.Float_Icon_Click)
                                startActivity(
                                    Intent(this@MainActivity, WebActivity::class.java)
                                        .putExtra(Constants.URL, BuildConfig.JS_URI + "cash.html")
                                )
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun filed() {
                    StatisticsUtil.onEvent(
                        applicationContext,
                        StatisticsConstants.Float_Icon_Permission,
                        StatisticsConstants.Label_Permission_Failed
                    )
                }
            })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //判断用户中心fragment是否显示 处理相应操作
            if (binding.frameUsercenterContainer.visibility != View.INVISIBLE) {
                binding.frameUsercenterContainer.visibility = View.INVISIBLE
                binding.nav.visibility = View.VISIBLE
                return true
            }
            //非新闻tab 先跳回news tab
            if (fragments[binding.vpContainer.currentItem] !is NewsFragment) {
                nav.toPosition(0)
                return true
            }
            //显示退出dialog弹窗
            FragmentUtil.getInstance().showFragmentDialog(this, null, DialogBackFragment())
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        floatWindowView?.unBind()
        handler?.removeCallbacks(runnable)
        handler = null
    }

}
