package com.lee.app

import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.app.adapter.ChatAdapter
import com.lee.app.databinding.ActivityChatListBinding
import com.lee.library.base.BaseVMActivity
import com.lee.library.extensions.keyboardObserver
import com.lee.library.extensions.adjustResizeStatusBar
import com.lee.library.extensions.reverseLayout
import com.lee.library.extensions.smoothScrollToTop
import com.lee.library.mvvm.base.BaseViewModel
import com.lee.library.tools.StatusTools
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author jv.lee
 * @date 2020/9/7
 * @description 聊天界面 沉浸式状态栏 输入法适配 方案二
 * 1.设置根布局 android:fitsSystemWindows="true"
 * 2.设置根布局 marginTop 为 负状态栏高度
 */
class ChatListActivity2 :
    BaseVMActivity<ActivityChatListBinding, BaseViewModel>(R.layout.activity_chat_list) {

    private var page = 0

    private val adapter by lazy { ChatAdapter(this, ArrayList()) }

    override fun bindView() {
        StatusUtil.setDarkStatusIcon(this)

        //适配沉浸式状态栏顶部弹起设置
        binding.constRoot.adjustResizeStatusBar(window, binding.toolbar.getStatusBarHeight())

        //设置recyclerView基础参数
        binding.rvContainer.adapter = adapter.proxy
        binding.rvContainer.layoutManager = LinearLayoutManager(this)

        //反转RecyclerView布局
        binding.rvContainer.reverseLayout()

        //监听键盘弹起
        window.keyboardObserver(openObserver = { binding.rvContainer.smoothScrollToTop() })

        //设置adapter基础配置
        adapter.initStatusView()
        adapter.pageCompleted()
        adapter.setAutoLoadMoreListener { requestData() }
    }

    override fun bindData() {
        requestData()
    }

    private fun requestData() {
        if (page == 6) {
            adapter.loadMoreEnd()
            return
        }
        launch {
            delay(1000)
            adapter.addData(arrayListOf<String>().also {
                for (index in 0..20) {
                    it.add("this is item data -> $index")
                }
            })
            adapter.notifyDataSetChanged()
            //recyclerView设置了stackFromEnd = true 来解决键盘滑动错位问题 所以第一次加载数据需手动滑动到第一行
            if (page == 0) {
                binding.rvContainer.scrollToPosition(0)
            }
            adapter.loadMoreCompleted()
            page++
        }
    }

}