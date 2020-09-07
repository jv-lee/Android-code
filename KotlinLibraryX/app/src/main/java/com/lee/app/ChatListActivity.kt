package com.lee.app

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.app.adapter.ChatAdapter
import com.lee.app.databinding.ActivityChatListBinding
import com.lee.library.base.BaseActivity
import com.lee.library.utils.KeyboardMoveHelper
import com.lee.library.utils.SizeUtil
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
        KeyboardMoveHelper(this, binding.root)

        binding.rvContainer.layoutManager = LinearLayoutManager(this)
        binding.rvContainer.adapter = adapter
//        KeyboardUtil.keyboardOpenMoveView(window, binding.constRoot)
        binding.rvContainer.postDelayed({
            binding.rvContainer.smoothScrollBy(0,SizeUtil.dp2px(this,100f))
        }, 3000)

    }

    override fun bindData() {

    }
}