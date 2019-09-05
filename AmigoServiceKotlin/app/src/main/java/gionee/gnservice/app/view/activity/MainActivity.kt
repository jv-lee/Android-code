package gionee.gnservice.app.view.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.gionee.gnservice.statistics.StatisticsUtil
import com.gionee.simple.UserCenterFragment
import com.lee.library.adapter.UiPagerAdapter
import com.lee.library.base.BaseActivity
import com.lee.library.livedatabus.InjectBus
import com.lee.library.livedatabus.LiveDataBus
import com.lee.library.utils.LogUtil
import com.lee.library.utils.StatusUtil
import com.lee.library.widget.nav.BottomNavView
import gionee.gnservice.app.Config
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.databinding.ActivityMainBinding
import gionee.gnservice.app.model.entity.Magnet
import gionee.gnservice.app.model.server.RetrofitUtils
import gionee.gnservice.app.tool.*
import gionee.gnservice.app.view.alert.DialogEx
import gionee.gnservice.app.view.fragment.*
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

    private var dialogActiveFragment: DialogActiveFragment? = null
    private var dialogUpdateFragment: DialogUpdateFragment? = null
    private var dialogBackFragment: DialogBackFragment? = null

    /**
     * 新手任务提示弹窗
     */
    private var dialog: AlertDialog? = null

    private val fragments by lazy {
        arrayOf(NewsFragment(), VideoFragment(), NovelFragment(), GameFragment(), WalletFragment())
    }

    /**
     * 通过Handler轮询 红包雨活动
     */
    private var handler: Handler? = Handler { msg -> false }
    private var runnable = Runnable { runnableFun() }
    private fun runnableFun() {
        if (Config.isPlayGame || Config.isPlayVideo) {
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
        supportFragmentManager.beginTransaction().add(R.id.frame_usercenter_container, UserCenterFragment()).commit()

        viewModel.apply {
            //推送奖励
            push.observe(this@MainActivity, Observer {
                if (it?.haveAward == 1) ToastTool.show(
                    this@MainActivity,
                    getString(R.string.notification_awared, it.count.toString())
                )
            })

            //推送跳转
            type.observe(this@MainActivity, Observer {
                startTabMode(it!!)
            })

            //红点提示
            redPoint.observe(this@MainActivity, Observer {
                if (it != null) binding.nav.setNumberDot(4, it.t1)
            })

            //磁铁or活动
            magnet.observe(this@MainActivity, Observer {
                if (it == null) return@Observer
                if (isDestroyed) return@Observer
                if (it.list.first_login == 1) showActive(it.list.skipurl)

                //加载磁贴图及设置磁贴行为
                if (it.list.isActive == 1) {
                    GlideTool.loadImage(it.list.icon, binding.ivActive)
                    binding.ivActive.setOnClickListener { v ->
                        StatisticsUtil.onEvent(this@MainActivity, "", "")
                        showActiveMode(it)
                    }
                }
            })

            tabIndex.observe(this@MainActivity, Observer {
                if (it?.menu?.index == 0) {
                    binding.ivActive.visibility = View.VISIBLE
                } else {
                    binding.ivActive.visibility = View.INVISIBLE
                }
                nav.toPosition(it?.menu?.index!!)
            })

            //新手奖励提示窗
            novice.observe(this@MainActivity, Observer {
                if (it == null) return@Observer
                if (it.isOverOpenClick == 1) {
                    dialog = DialogEx(
                        this@MainActivity,
                        DialogEx.ViewInterface {
                            val view =
                                LayoutInflater.from(this@MainActivity).inflate(R.layout.alert_task_first_open, null)
                            view.findViewById<ImageView>(R.id.closeBtn).setOnClickListener {
                                dialog?.dismiss()
                            }
                            view
                        }).build()
                }
            })
        }

        //初始化配置及事件统计,及磁贴
        viewModel.getConfig()
        viewModel.initEvent(intent)
        viewModel.initPush(intent)
        viewModel.initMagnetActive()

        PrefAccess.isGuide(binding.guideNews, PrefAccess.KEY_GUIDE_FINISHED_NEWS)
        PrefAccess.isGuide(binding.guideVideo, PrefAccess.KEY_GUIDE_FINISHED_VIDEO)
        PrefAccess.isGuide(binding.guideNovel, PrefAccess.KEY_GUIDE_FINISHED_NOVEL)

        initRedPoint(0)
        initUpdateAlert()
    }

    override fun bindView() {
        binding.vpContainer.adapter = UiPagerAdapter(supportFragmentManager, fragments)
        binding.vpContainer.setNoScroll(true)
        binding.vpContainer.offscreenPageLimit = fragments.size - 1

        binding.nav.itemIconTintList = null
        binding.nav.bindViewPager(binding.vpContainer)
        binding.nav.setItemPositionListener(this@MainActivity)

        viewModel.initTabIndex(intent)
    }

    /**
     * 导航tab选中监听
     */
    override fun onPosition(menuItem: MenuItem?, position: Int) {
        when (position) {
            0 -> {
                LogUtil.i("nav -> news")
                binding.guideNews.visibility = View.GONE
            }
            1 -> {
                LogUtil.i("nav -> video")
                binding.guideVideo.visibility = View.GONE
            }
            2 -> {
                LogUtil.i("nav -> novel")
                binding.guideNovel.visibility = View.GONE
            }
            3 -> {
                LogUtil.i("nav -> game")
            }
            4 -> {
                LogUtil.i("nav -> wallet")
            }
        }
    }

    /**
     * TODO 刷新红点提示事件
     */
    @InjectBus(EventConstants.UPDATE_RED_POINT)
    fun initRedPoint(code: Int) {
        viewModel.initRedPoint()
    }

    /**
     * TODO 跳转tab事件
     */
    @InjectBus(EventConstants.START_PAGE)
    fun startTabMode2(code: Int) {
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

    /**
     * TODO 启动红包雨事件
     */
    @InjectBus(EventConstants.RED_PACKAGE_ACTIVITY)
    fun showRedPackageActivity(code: Int) {
        if (Config.nextDT != 0) {
            handler?.removeCallbacks(runnable)
            handler?.postDelayed(runnable, (Config.nextDT * 1000).toLong())
        }
    }

    /**
     * TODO 首次打开奖励
     */
    @InjectBus(EventConstants.NOVICE_AWARD)
    fun showNoviceAward(code: Int) {
        if (PrefAccess.isFirstOpen()) viewModel.initNoviceAward()
    }

    fun startTabMode(code: Int) {
        if (code != 5) {
            nav.toPosition(code)
        }else{
            //钱包任务栏
            nav.toPosition(4)
        }
    }

    private fun showActiveMode(entity: Magnet) {
        when (entity.list.way) {
            //内部页面跳转
//            Constants.MAGNET_TYPE_TAB -> startTabMode2(LiveEvent<Int>().data = entity.list.skip)
            //H5游戏跳转
            Constants.MAGNET_TYPE_GAME -> startActivity(
                Intent(this, GameActivity::class.java)
                    .putExtra(Constants.URL, entity.list.game.linkurl)
                    .putExtra(Constants.V_TYPE, entity.list.game.landspace)
            )
            //活动页面跳转
            Constants.MAGNET_TYPE_ACTIVE -> showActive(entity.list.skipurl)
            Constants.MAGNET_TYPE_SDK_H5 -> startActivity(
                Intent(this, WebActivity::class.java)
                    .putExtra(Constants.URL, entity.list.skipurl)
                    .putExtra(Constants.V_TYPE, 1)
                    .putExtra(Constants.TITLE, entity.list.name)
            )
        }
    }

    /**
     * TODO 初始化更新提示 或 红包雨和磁贴
     */
    private fun initUpdateAlert() {
        if (PrefAccess.isOpenUpdate()) {
            showUpdate()
        } else {
            //显示红包雨
            DelayTimeTool.timeChange(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    initRedPackage()
                }
            }, 1000, 0, 1)
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
     * TODO 活动弹窗
     */
    private fun showActive(url: String) {
        if (isDestroyed) return
        if (dialogActiveFragment == null) {
            dialogActiveFragment = DialogActiveFragment.get(url)
        }
        if (dialogActiveFragment!!.isAdded) {
            supportFragmentManager.beginTransaction().remove(dialogActiveFragment!!).commit()
        }
        dialogActiveFragment?.show(supportFragmentManager, dialogActiveFragment!!::class.java.simpleName)
    }

    /**
     * TODO 更新弹窗
     */
    private fun showUpdate() {
        if (isDestroyed) return
        if (dialogUpdateFragment == null) {
            dialogUpdateFragment = DialogUpdateFragment()
        }
        if (dialogUpdateFragment!!.isAdded) {
            supportFragmentManager.beginTransaction().remove(dialogUpdateFragment!!).commit()
        }
        dialogUpdateFragment?.show(supportFragmentManager, dialogUpdateFragment!!::class.java.simpleName)
    }

    /**
     * TODO 退出弹窗
     */
    private fun showBack() {
        if (isDestroyed) return
        if (dialogBackFragment == null) {
            dialogBackFragment = DialogBackFragment()
        }
        if (dialogBackFragment!!.isAdded) {
            supportFragmentManager.beginTransaction().remove(dialogBackFragment!!).commit()
        }
        dialogBackFragment?.show(supportFragmentManager, dialogBackFragment!!::class.java.simpleName)
    }

    fun showUserCenter() {
        binding.nav.visibility = View.INVISIBLE
        binding.frameUsercenterContainer.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacks(runnable)
        handler = null
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
            showBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
