package com.lee.app

import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.app.adapter.ChatAdapter
import com.lee.app.databinding.ActivityChatListBinding
import com.lee.library.adapter.base.BaseViewAdapter
import com.lee.library.base.BaseBindingActivity
import com.lee.library.extensions.reverseLayout
import com.lee.library.extensions.smoothScrollToTop
import com.lee.library.tools.SystemBarTools.parentTouchHideSoftInput
import com.lee.library.tools.SystemBarTools.setDarkStatusIcon
import com.lee.library.tools.SystemBarTools.softInputBottomPaddingChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 聊天界面 沉浸式状态栏 输入法适配
 * @author jv.lee
 * @date 2020/9/7
 */
class ChatListActivity :
    BaseBindingActivity<ActivityChatListBinding>() {

    private var page = 0

    private val adapter by lazy { ChatAdapter(this) }

    override fun bindView() {
        // 深色状态栏icon
        window.setDarkStatusIcon()

        // 监听键盘弹起设置padding及回滚至最新消息
        window.parentTouchHideSoftInput(mBinding.rvContainer)
        mBinding.root.softInputBottomPaddingChange(
            open = { mBinding.rvContainer.smoothScrollToTop() }
        )

        // 设置recyclerView基础参数
        mBinding.rvContainer.adapter = adapter.getProxy()
        mBinding.rvContainer.layoutManager = LinearLayoutManager(this)
        mBinding.rvContainer.reverseLayout() // 反转列表top->bottom

        // 设置adapter基础配置
        adapter.initStatusView()
        adapter.pageCompleted()
        adapter.setAutoLoadMoreListener(object : BaseViewAdapter.AutoLoadMoreListener {
            override fun autoLoadMore() {
                requestData()
            }
        })
    }

    override fun bindData() {
        requestData()
    }

    private fun requestData() {
        if (page == 6) {
            adapter.loadMoreEnd()
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            if (page != 0) delay(1000)
            val data = arrayListOf<String>().apply {
                for (index in 0..20) {
                    add("this is item data -> $index")
                }
            }
            adapter.addData(data)
            adapter.loadMoreCompleted()
            page++
        }
    }
}