package com.lee.app

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.app.adapter.ChatAdapter
import com.lee.app.databinding.ActivityChatListBinding
import com.lee.library.base.BaseActivity
import com.lee.library.utils.KeyboardHelper
import com.lee.library.utils.StatusUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ChatListActivity :
    BaseActivity<ActivityChatListBinding, ViewModel>(R.layout.activity_chat_list) {

    private val adapter by lazy {
        ChatAdapter(this, ArrayList<String>().also {
            for (index in 0..30) {
                it.add("this is item data -> $index")
            }
        })
    }

    private val keyboardHelper by lazy { KeyboardHelper(window.decorView, binding.root) }

    override fun bindView() {
        StatusUtil.setStatusFontLight2(this)

        binding.rvContainer.adapter = adapter
        binding.rvContainer.layoutManager = LinearLayoutManager(this)

        //先绑定recyclerView
        keyboardHelper.bindRecyclerView(binding.rvContainer, true)
        //后开启输入法弹起布局自适应
        keyboardHelper.enable()
    }

    override fun bindData() {

    }

    @ExperimentalCoroutinesApi
    override fun onDestroy() {
        keyboardHelper.disable()
        super.onDestroy()
    }
}