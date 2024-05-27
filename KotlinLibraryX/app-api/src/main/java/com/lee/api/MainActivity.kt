package com.lee.api

import android.Manifest
import android.content.Intent
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.component3
import androidx.core.graphics.component4
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import com.lee.api.activity.DataStorePreferenceActivity
import com.lee.api.activity.DataStoreProtoActivity
import com.lee.api.activity.StartResultActivity
import com.lee.api.activity.WindowInsetsActivity
import com.lee.api.databinding.ActivityMainBinding
import com.lee.api.fragment.StartFragmentActivity
import com.lee.library.base.BaseBindingActivity
import com.lee.library.extensions.toast
import com.lee.library.tools.PermissionLauncher

/**
 * google new api demo
 */
class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    private val permissionLauncher = PermissionLauncher(this)

    override fun bindView() {
        mBinding.btnFragmentResult.setOnClickListener {
            startActivity(Intent(this, StartFragmentActivity::class.java))
        }
        mBinding.btnActivityResult.setOnClickListener {
            startActivity(Intent(this, StartResultActivity::class.java))
        }
        mBinding.btnActivityDataStore.setOnClickListener {
            startActivity(Intent(this, DataStorePreferenceActivity::class.java))
        }
        mBinding.btnActivityDataStoreProto.setOnClickListener {
            startActivity(Intent(this, DataStoreProtoActivity::class.java))
        }
        mBinding.btnWindowInsets.setOnClickListener {
            startActivity(Intent(this, WindowInsetsActivity::class.java))
        }
        mBinding.btnRequestPermission.setOnClickListener {
            permissionLauncher.requestPermissions(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                successCall = {
                    toast("request 成功")
                },
                cancelCall = {
                    toast("request $it 取消")
                },
                disableCall = {
                    toast("request $it 禁止")
                }
            )
        }
    }

    override fun bindData() {
    }

    private fun ktxCoreApi() {
        // ktx-core 扩展函数 支持drawable to bitmap ，bitmap to drawable ，bitmap apply canvas draw
        var bitmap =
            ContextCompat.getDrawable(this, R.mipmap.ic_launcher_round)?.toBitmap()?.applyCanvas {
                drawLine(0f, 0f, 100f, 100f, Paint())
            }

        bitmap = createBitmap(100, 100).apply {
            scale(50, 50)
        }

        // 解构赋值 直接获取rect l，t，r，b
        val rect = Rect()
        val (left, top, right, bottom) = rect
    }
}
