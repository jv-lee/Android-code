package com.lee.app

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.app.adapter.ChatAdapter
import com.lee.app.databinding.ActivityChatListBinding
import com.lee.library.base.BaseVMActivity
import com.lee.library.extensions.reverseLayout
import com.lee.library.extensions.smoothScrollToTop
import com.lee.library.tools.KeyboardTools.keyboardObserver
import com.lee.library.tools.KeyboardTools.keyboardPaddingBottom
import com.lee.library.tools.StatusTools.setDarkStatusIcon
import com.lee.library.tools.StatusTools.statusBar
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
    BaseVMActivity<ActivityChatListBinding, ViewModel>(R.layout.activity_chat_list) {

    private var page = 0

    private val adapter by lazy { ChatAdapter(this, ArrayList()) }

    override fun bindView() {
        window.statusBar()
        window.setDarkStatusIcon()

        //设置recyclerView基础参数
        binding.rvContainer.adapter = adapter.proxy
        binding.rvContainer.layoutManager = LinearLayoutManager(this)
        binding.rvContainer.reverseLayout()

        //监听键盘弹起设置padding 及 回滚至最新消息
        binding.root.keyboardPaddingBottom()
        binding.root.keyboardObserver(open = { binding.rvContainer.smoothScrollToTop() })

        //设置adapter基础配置
        adapter.initStatusView()
        adapter.pageCompleted()
        adapter.setAutoLoadMoreListener { requestData() }
    }

    override fun bindData() {
        requestData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private  fun requestData() {
        if (page == 6) {
            adapter.loadMoreEnd()
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
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