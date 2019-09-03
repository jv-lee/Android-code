package gionee.gnservice.app.view.fragment

import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import com.android.droi.books.utils.AsyncHandler.runOnUiThread
import com.android.droi.books.utils.Constant
import com.cmcm.cmgame.CmGameSdk
import com.cmcm.cmgame.IGamePlayTimeCallback
import com.google.gson.Gson
import com.lee.library.base.BaseFragment
import gionee.gnservice.app.BuildConfig
import gionee.gnservice.app.R
import gionee.gnservice.app.constants.Constants
import gionee.gnservice.app.databinding.FragmentGameBinding
import gionee.gnservice.app.view.activity.GameActivity
import gionee.gnservice.app.view.native.ADInterface
import gionee.gnservice.app.view.native.JSInterface

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description 主TAB 游戏板块 （H5）
 */
class GameFragment : BaseFragment<FragmentGameBinding, ViewModel>(R.layout.fragment_game, null) {

    override fun bindData(savedInstanceState: Bundle?) {
        // 初始化小游戏 sdk 的账号数据，用于存储游戏内部的用户数据，
        // 为避免数据异常，这个方法建议在小游戏列表页面展现前（可以是二级页面）才调用
        CmGameSdk.initCmGameAccount()
        CmGameSdk.setGamePlayTimeCallback(IGamePlayTimeCallback { gameId, playTimeInSeconds ->

        })
        binding.web.addJavascriptInterface(JSInterface(context!!.applicationContext), "client")
        binding.web.addJavascriptInterface(this, "game")
        binding.web.addJavascriptInterface(ADInterface(activity!!), "ad")
        binding.web.loadUrl(BuildConfig.GAME_URI)
    }

    override fun bindView() {
    }

    override fun lazyLoad() {
        super.lazyLoad()
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

    }

    override fun onFragmentResume() {
        super.onFragmentResume()
        binding.web.loadUrl("javascript:facthGameData()")
    }


}