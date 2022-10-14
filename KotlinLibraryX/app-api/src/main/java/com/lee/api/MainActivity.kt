package com.lee.api

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.lee.api.activity.DataStorePreferenceActivity
import com.lee.api.activity.DataStoreProtoActivity
import com.lee.api.activity.StartResultActivity
import com.lee.api.activity.WindowInsetsActivity
import com.lee.api.databinding.ActivityMainBinding
import com.lee.api.fragment.StartFragmentActivity
import com.lee.library.base.BaseVMActivity
import com.lee.library.extensions.toast
import com.lee.library.tools.PermissionLauncher

class MainActivity : BaseVMActivity<ActivityMainBinding, ViewModel>(R.layout.activity_main) {

    private val permissionLauncher = PermissionLauncher(this)

    @SuppressLint("NewApi")
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
        binding.btnWindowInsets.setOnClickListener {
            startActivity(Intent(this, WindowInsetsActivity::class.java))
        }
        binding.btnRequestPermission.setOnClickListener {
            permissionLauncher.requestPermissions(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                successCall = {
                    toast("request 成功")
                }, cancelCall = {
                    toast("request $it 取消")
                }, disableCall = {
                    toast("request $it 禁止")
                })
        }
    }

    override fun bindData() {

    }
}
