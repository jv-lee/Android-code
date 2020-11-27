package com.lee.api.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.lee.api.R
import com.lee.api.databinding.ActivityStartResultBinding
import com.lee.library.base.BaseActivity
import com.lee.library.mvvm.base.BaseViewModel
import java.io.File

class StartResultActivity :
    BaseActivity<ActivityStartResultBinding, BaseViewModel>(R.layout.activity_start_result) {

    private val dataResult = prepareCall(ActivityResultContracts.StartActivityForResult()) {
        toast(it?.data?.getStringExtra("value") ?: "")
    }

    private val permissionsResult =
        prepareCall(ActivityResultContracts.RequestPermissions()) { it ->
            it ?: return@prepareCall
            it.forEach {
                toast("${it.key} request ${it.value}")
            }
        }

    private val pictureResult = prepareCall(ActivityResultContracts.TakePicture()) {
        binding.ivPicture.setImageBitmap(it)
    }

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
            val path =
//                filesDir.absolutePath + File.separator + "img" + File.separator + System.currentTimeMillis() + File.separator + ".jpg"
//                filesDir.absolutePath + File.separator + System.currentTimeMillis() + File.separator + ".jpg"
                Environment.getExternalStorageDirectory().absolutePath + File.separator + System.currentTimeMillis() + ".jpg"
            val file = File(path)

            //文件创建操作
//            if (!file.parentFile.exists()) file.parentFile.mkdir()
//            if (!file.exists()) file.createNewFile()

            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
            } else {
                Uri.fromFile(file)
            }

//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//            startActivityForResult(intent, 1)
            pictureResult.launch(null)
        }
    }

    override fun bindData() {

    }

}