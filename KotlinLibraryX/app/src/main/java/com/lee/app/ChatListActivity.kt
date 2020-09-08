package com.lee.app

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.app.adapter.ChatAdapter
import com.lee.app.databinding.ActivityChatListBinding
import com.lee.library.base.BaseActivity
import com.lee.library.utils.KeyboardHelper
import com.lee.library.utils.StatusUtil

class ChatListActivity :
    BaseActivity<ActivityChatListBinding, ViewModel>(R.layout.activity_chat_list) {

    private val adapter by lazy {
        ChatAdapter(this, ArrayList<String>().also {
            for (index in 0..30) {
                it.add("this is item data -> $index")
            }
        })
    }

    override fun bindView() {
        StatusUtil.setStatusFontLight2(this)
        KeyboardHelper(window.decorView, binding.root).enable(true)

        binding.rvContainer.layoutManager = LinearLayoutManager(this).also {
            it.reverseLayout = true
        }
        binding.rvContainer.adapter = adapter

    }

    override fun bindData() {

    }
}