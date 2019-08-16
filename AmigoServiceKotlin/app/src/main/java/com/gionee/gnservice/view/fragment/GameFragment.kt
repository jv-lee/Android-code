package com.gionee.gnservice.view.fragment

import android.os.Bundle
import com.cmcm.cmgame.CmGameSdk
import com.gionee.gnservice.app.R
import com.lee.library.base.BaseFragment
import com.lee.library.ioc.annotation.ContentView

/**
 * @author jv.lee
 * @date 2019/8/14.
 * @description
 */
@ContentView(R.layout.fragment_game)
class GameFragment : BaseFragment() {

    override fun bindData(savedInstanceState: Bundle?) {
        // 初始化小游戏 sdk 的账号数据，用于存储游戏内部的用户数据，
        // 为避免数据异常，这个方法建议在小游戏列表页面展现前（可以是二级页面）才调用
        CmGameSdk.initCmGameAccount()
    }

    override fun bindView() {
    }


    override fun lazyLoad() {
//        CmGameSdk.startH5Game("tanchisheduiduipeng")
    }

}