package com.lee.api.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.lee.api.R
import com.lee.api.databinding.ActivityStartResultBinding
import com.lee.library.base.BaseVMActivity
import com.lee.library.extensions.toast
import com.lee.library.mvvm.base.BaseViewModel
import java.io.File

class StartResultActivity :
    BaseVMActivity<ActivityStartResultBinding, BaseViewModel>(R.layout.activity_start_result) {

    private val dataResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            toast(it?.data?.getStringExtra("value") ?: "")
        }

    private val permissionsResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
            it ?: return@registerForActivityResult
            it.forEach {
                toast("${it.key} request ${it.value}")
            }
        }

    private val pictureResult = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            binding.ivPicture.setImageURI(uri)
        }
    }

    private var uri: Uri? = null

    override fun bindView() {
        binding.btnActivityResult.setOnClickListener {
            dataResult.launch(Intent(this, ResultActivity::class.java))
        }
        binding.btnPermissionsResult.setOnClickListener {
            permissionsResult.launch(arrayOf(Manifest.permission.CAMERA))
        }
        binding.btnPictureResult.setOnClickListener {
            //权限校验
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                permissionsResult.launch(arrayOf(Manifest.permission.CAMERA))
                return@setOnClickListener
            }

            //设置文件路径创建文件对象
            val fileDir = File(filesDir.absolutePath, "image")
            if (!fileDir.exists()) fileDir.mkdir()
            val file = File(fileDir.absolutePath, "${System.currentTimeMillis()}.jpg")

            //文件创建操作
            if (!file.parentFile.exists()) file.parentFile.mkdir()

            uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
            } else {
                Uri.fromFile(file)
            }

//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//            startActivityForResult(intent, 1)
            pictureResult.launch(uri)
        }
    }

    override fun bindData() {

    }

}