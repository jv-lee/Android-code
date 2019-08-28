package gionee.gnservice.app.view.fragment

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import com.cmcm.cmgame.CmGameSdk
import com.lee.library.base.BaseFragment
import gionee.gnservice.app.R
import gionee.gnservice.app.databinding.FragmentGameBinding

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description
 */
class GameFragment : BaseFragment<FragmentGameBinding,ViewModel>(R.layout.fragment_game,null) {

    override fun bindData(savedInstanceState: Bundle?) {
        // 初始化小游戏 sdk 的账号数据，用于存储游戏内部的用户数据，
        // 为避免数据异常，这个方法建议在小游戏列表页面展现前（可以是二级页面）才调用
        CmGameSdk.initCmGameAccount()
        binding.web.loadUrl("file:///android_asset/html/game/index.html")
    }

    override fun bindView() {
    }

}