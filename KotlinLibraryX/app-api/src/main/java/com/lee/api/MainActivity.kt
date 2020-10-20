package com.lee.api

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.lee.api.databinding.ActivityMainBinding
import com.lee.library.base.BaseActivity
import com.lee.library.mvvm.base.BaseViewModel

class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>(R.layout.activity_main) {

    private val activityForResult = prepareCall(ActivityResultContracts.StartActivityForResult()) {
        toast(it?.data?.getStringExtra("value") ?: "")
    }

    override fun bindView() {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, MainFragment())
            .commit()

        binding.btnActivityResult.setOnClickListener {
            activityForResult.launch(Intent(this, ResultActivity::class.java))
        }
    }

    override fun bindData() {

    }
}
