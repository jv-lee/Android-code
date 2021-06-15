package com.lee.app

import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.app.adapter.ChatAdapter
import com.lee.app.databinding.ActivityChatListBinding
import com.lee.library.base.BaseVMActivity
import com.lee.library.mvvm.base.BaseViewModel
import com.lee.library.utils.KeyboardHelper
import com.lee.library.utils.StatusUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author jv.lee
 * @date 2020/9/7
 * @description 聊天界面 沉浸式状态栏 输入法适配
 */
class ChatListActivity :
    BaseVMActivity<ActivityChatListBinding, BaseViewModel>(R.layout.activity_chat_list) {

    private var page = 0

    private val adapter by lazy { ChatAdapter(this, ArrayList()) }

    private val keyboardHelper by lazy { KeyboardHelper(window.decorView, binding.root) }

    override fun bindView() {
        StatusUtil.setDarkStatusIcon(this)

        //设置recyclerView基础参数
        binding.rvContainer.adapter = adapter.proxy
        binding.rvContainer.layoutManager = LinearLayoutManager(this)
        binding.rvContainer.reverseLayout()

        //先绑定recyclerView 后开启输入法弹起布局自适应
        keyboardHelper.bindRecyclerView(binding.rvContainer, true)
        keyboardHelper.enable()

        //设置adapter基础配置
        adapter.initStatusView()
        adapter.pageCompleted()
        adapter.setAutoLoadMoreListener { requestData() }
    }

    override fun bindData() {
        requestData()
    }

    @ExperimentalCoroutinesApi
    override fun onDestroy() {
        keyboardHelper.disable()
        super.onDestroy()
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