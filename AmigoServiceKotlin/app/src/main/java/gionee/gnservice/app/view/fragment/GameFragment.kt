package gionee.gnservice.app.view.fragment

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import com.cmcm.cmgame.CmGameSdk
import com.cmcm.cmgame.IGamePlayTimeCallback
import com.google.gson.Gson
import com.lee.library.base.BaseFragment
import com.lee.library.livedatabus.InjectBus
import com.lee.library.livedatabus.LiveDataBus
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.Config
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.constants.EventConstants
import gionee.gnservice.app.databinding.FragmentGameBinding
import gionee.gnservice.app.tool.ToastTool
import gionee.gnservice.app.tool.ValueTimer
import gionee.gnservice.app.view.activity.GameActivity
import gionee.gnservice.app.view.activity.MainActivity
import gionee.gnservice.app.view.native.ADInterface
import gionee.gnservice.app.view.native.JSInterface
import gionee.gnservice.app.vm.TimeViewModel

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description 主TAB 游戏板块 （H5）
 */
class GameFragment :
    BaseFragment<FragmentGameBinding, TimeViewModel>(R.layout.fragment_game, TimeViewModel::class.java) {

    private val value by lazy {
        ValueTimer.init(30, object : ValueTimer.TimeCallback {
            override fun endRepeat() {
                viewModel.subAddTime("30", Constants.TYPE_GAME, Config.playGameId)
            }
        })
    }

    override fun bindData(savedInstanceState: Bundle?) {
        LiveDataBus.getInstance().injectBus(this)

        viewModel.taskInfo.observe(this, Observer {
            if (it?.taskOverList != null && it.taskOverList.isNotEmpty()) {
                for (taskOver in it.taskOverList) {
                    if (taskOver.taskType == 2) {
                        ToastTool.show(activity!!, getString(R.string.task_name, taskOver.taskName, taskOver.taskCount))
                        binding.web.loadUrl("javascript:ydChange(${taskOver.taskCount})")
                        LiveDataBus.getInstance().getChannel(EventConstants.UPDATE_RED_POINT).value = 0
                    }
                }
            }
        })

        // 初始化小游戏 sdk 的账号数据，用于存储游戏内部的用户数据，
        // 为避免数据异常，这个方法建议在小游戏列表页面展现前（可以是二级页面）才调用
        CmGameSdk.initCmGameAccount()
        CmGameSdk.setGamePlayTimeCallback(IGamePlayTimeCallback { gameId, playTimeInSeconds ->
            viewModel.subAddTime(playTimeInSeconds.toString(), Constants.TYPE_GAME, gameId)
        })
    }

    override fun bindView() {
        binding.web.addJavascriptInterface(JSInterface(context!!.applicationContext), JSInterface.NAME)
        binding.web.addJavascriptInterface(this, "game")
        binding.web.addJavascriptInterface(ADInterface(activity!!, binding.web), ADInterface.NAME)
        binding.web.loadUrl(BuildConfig.GAME_URI)
    }

    @JavascriptInterface
    fun loadGameList(): String {
        return Gson().toJson(CmGameSdk.getGameInfoList())
    }

    @JavascriptInterface
    fun hasGame(gameID: String): Boolean {
        return CmGameSdk.hasGame(gameID)
    }

    @JavascriptInterface
    fun startGame(gameID: String) {
        CmGameSdk.startH5Game(gameID)
    }

    @JavascriptInterface
    fun startGame(url: String, vtype: Int, gid: String) {
        activity?.runOnUiThread {
            Config.playGameId = gid
            startActivity(
                Intent(activity, GameActivity::class.java)
                    .putExtra(Constants.URL, url)
                    .putExtra(Constants.V_TYPE, vtype)
            )
        }
    }

    @JavascriptInterface
    fun refreshUrl() {
        activity?.runOnUiThread {
            binding.web.reload()
        }
    }

    @JavascriptInterface
    fun startPage(code: Int, arg: String) {
        activity?.runOnUiThread {
            (activity as MainActivity).startTabMode(code)
        }
    }

    override fun onFragmentResume() {
        super.onFragmentResume()
        binding.web.loadUrl("javascript:facthGameData()")
    }

    override fun onDestroy() {
        ValueTimer.destroy(value)
        super.onDestroy()
    }

    @InjectBus(value = EventConstants.GAME_TIMER_STATUS)
    fun time(run: Int) {
        if (run == 1) {
            ValueTimer.resume(value)
        } else {
            ValueTimer.pause(value)
        }
    }


}