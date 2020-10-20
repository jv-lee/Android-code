package com.lee.api.activity

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.lee.api.R
import com.lee.api.databinding.ActivityStartResultBinding
import com.lee.library.base.BaseActivity
import com.lee.library.mvvm.base.BaseViewModel

class StartResultActivity :
    BaseActivity<ActivityStartResultBinding, BaseViewModel>(R.layout.activity_start_result) {

    private val activityForResult = prepareCall(ActivityResultContracts.StartActivityForResult()) {
        toast(it?.data?.getStringExtra("value") ?: "")
    }

    override fun bindView() {
        binding.btnActivityResult.setOnClickListener {
            activityForResult.launch(Intent(this, ResultActivity::class.java))
        }
    }

    override fun bindData() {

    }
}