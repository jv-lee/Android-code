package com.lee.app

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.lee.app.adapter.FormAdapter
import com.lee.app.databinding.ActivityFormTableBinding
import com.lee.library.base.BaseActivity
import com.lee.library.utils.KeyboardMoveHelper
import com.lee.library.utils.StatusUtil

class FormTableActivity :
    BaseActivity<ActivityFormTableBinding, ViewModel>(R.layout.activity_form_table) {
    private val adapter by lazy {
        FormAdapter(this, ArrayList<String>().also {
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
    }

    override fun bindData() {

    }
}