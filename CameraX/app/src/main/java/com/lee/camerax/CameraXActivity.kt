package com.lee.camerax

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.*
import com.lee.camerax.base.BaseActivity
import com.lee.camerax.databinding.ActivityCameraxBinding
import com.lee.camerax.manager.CameraXManager

/**
 * @author jv.lee
 * @date 2021/4/2
 * @description
 */
class CameraXActivity : BaseActivity<ActivityCameraxBinding>() {

    private val mCameraXManager by lazy { CameraXManager() }

    override fun bindViewBinding(): ActivityCameraxBinding {
        return ActivityCameraxBinding.inflate(layoutInflater)
    }

    @SuppressLint("MissingPermission")
    override fun bindView() {
        //初始化Camera
        requestPermission(Manifest.permission.CAMERA, {
            mCameraXManager.initCamera(binding.previewView, this)
        }, { toast(it) })

        //设置摄像头切换
        binding.ivCameraSwitch.setOnClickListener {
            mCameraXManager.switchCamera()
        }

        //设置摄像模式切换
        binding.switchVideo.setOnCheckedChangeListener { buttonView, isChecked ->
            requestPermission(Manifest.permission.RECORD_AUDIO, {
                mCameraXManager.switchVideo(isChecked)
            }, { toast(it) })
        }

        //拍摄快门键
        binding.ivTakePicture.setOnClickListener {
            if (mCameraXManager.isVideoMode()) startRecord() else takePicture()
        }

        //开启图像分析
        binding.ivQrCode.setOnClickListener {
            analyzeImage()
        }
    }

    @SuppressLint("MissingPermission")
    private fun takePicture() {
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, {
            mCameraXManager.takePicture(object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    toast(outputFileResults.savedUri?.toString())
                }

                override fun onError(exception: ImageCaptureException) {
                    toast(exception.message)
                }
            })
        }, { toast(it) })
    }

    @SuppressLint("MissingPermission")
    private fun startRecord() {
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, {
            mCameraXManager.startRecord(object : VideoCapture.OnVideoSavedCallback {
                override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                    toast(outputFileResults.savedUri?.toString())
                }

                override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                    toast(message)
                }

            })
        }, { toast(it) })
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun analyzeImage() {
        mCameraXManager.analyzeImage(ImageAnalysis.Analyzer {
            Log.i("CameraXActivity", "analyze: $it")
        })
    }

}