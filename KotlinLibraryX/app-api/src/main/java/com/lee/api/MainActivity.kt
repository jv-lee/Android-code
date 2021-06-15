package com.lee.api

import android.content.Intent
import com.lee.api.activity.DataStorePreferenceActivity
import com.lee.api.activity.DataStoreProtoActivity
import com.lee.api.activity.StartResultActivity
import com.lee.api.databinding.ActivityMainBinding
import com.lee.api.fragment.StartFragmentActivity
import com.lee.library.base.BaseVMActivity
import com.lee.library.mvvm.base.BaseViewModel

class MainActivity : BaseVMActivity<ActivityMainBinding, BaseViewModel>(R.layout.activity_main) {

    override fun bindView() {
        binding.btnFragmentResult.setOnClickListener {
            startActivity(Intent(this, StartFragmentActivity::class.java))
        }
        binding.btnActivityResult.setOnClickListener {
            startActivity(Intent(this, StartResultActivity::class.java))
        }
        binding.btnActivityDataStore.setOnClickListener {
            startActivity(Intent(this, DataStorePreferenceActivity::class.java))
        }
        binding.btnActivityDataStoreProto.setOnClickListener {
            startActivity(Intent(this, DataStoreProtoActivity::class.java))
        }
    }

    override fun bindData() {

    }
}
