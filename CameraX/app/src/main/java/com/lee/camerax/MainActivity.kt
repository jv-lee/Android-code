package com.lee.camerax

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.provider.MediaStore
import android.view.SoundEffectConstants
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.lee.camerax.base.BaseActivity
import com.lee.camerax.base.dp2px
import com.lee.camerax.databinding.ActivityMainBinding
import org.jetbrains.annotations.NotNull

/**
 * @author jv.lee
 * @date 2021/3/23
 * @description CameraX 示例
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var mCameraProvider: ProcessCameraProvider? = null
    private var mCamera: Camera? = null

    private var mPreview: Preview? = null
    private var mImageCapture: ImageCapture? = null
    private var mImageAnalysis: ImageAnalysis? = null
    private var mVideoCapture: VideoCapture? = null

    private var isBack = true
    private var isVideo = false
    private var isRecording = false

    override fun bindViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun bindView() {
        initCamera()
        bindSwitchVideo()
    }

    private fun bindSwitchVideo() {
        binding.switchVideo.setOnCheckedChangeListener { buttonView, isChecked ->
            requestPermission(Manifest.permission.RECORD_AUDIO, {
                mCameraProvider?.let {
                    stopRecording()
                    isVideo = isChecked
                    bindPreview(it, binding.previewView)
                }
            }, { toast(it) })
        }
    }

    //切换摄像头
    fun onSwitchCamera(view: View) {
        mCameraProvider?.let {
            isBack = !isBack
            bindPreview(it, binding.previewView)
        }
    }

    //拍照
    fun onTakePicture(view: View) {
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, {
            if (isVideo) {
                recordVideo()
            } else {
                takePictureInternal()
            }
        }, { toast(it) })
    }

    //初始化相机
    private fun initCamera() {
        requestPermission(Manifest.permission.CAMERA, {
            setUpCamera(binding.previewView)
        }, { toast(it) })
    }

    /**
     * 设置相机提供者 启动绑定预览
     */
    private fun setUpCamera(previewView: PreviewView) {
        ProcessCameraProvider.getInstance(this).let { processCameraProvider ->
            processCameraProvider.addListener(Runnable {
                try {
                    mCameraProvider = processCameraProvider.get()
                    //绑定预览视图
                    bindPreview(mCameraProvider, previewView)
                    //绑定触摸聚焦
                    bindTouchPreview()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(this))
        }
    }

    /**
     * 绑定预览视图
     */
    @SuppressLint("RestrictedApi")
    private fun bindPreview(
        @NotNull cameraProvider: ProcessCameraProvider?,
        previewView: PreviewView
    ) {
        cameraProvider ?: return
        //创建预览配置
        mPreview = Preview.Builder().build()
        //创建拍照配置
        mImageCapture = ImageCapture
            .Builder()
            .setTargetRotation(previewView.display.rotation)
            .build()
        //创建录像配置
        mVideoCapture = VideoCapture.Builder()
            .setTargetRotation(previewView.display.rotation)
            .setVideoFrameRate(25)
            .setBitRate(3 * 1024 * 1024)
            .build()

        //设置前置/后置摄像头
        val cameraSelector = if (isBack) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }

        //清除所有绑定
        cameraProvider.unbindAll()

        //创建相机
        mCamera = if (isVideo) {
            cameraProvider.bindToLifecycle(this, cameraSelector, mPreview, mVideoCapture)
        } else {
            cameraProvider.bindToLifecycle(this, cameraSelector, mPreview, mImageCapture)
        }

        //设置视图
        mPreview?.setSurfaceProvider(previewView.surfaceProvider)
    }

    /**
     * 预览视图触摸事件 提供聚焦功能
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun bindTouchPreview() {
        binding.previewView.setOnTouchListener { v, event ->
            val action = FocusMeteringAction.Builder(
                binding.previewView.meteringPointFactory.createPoint(
                    event.x,
                    event.y
                )
            ).build()
            try {
                showFocusView(event.x.toInt(), event.y.toInt())
                mCamera?.cameraControl?.startFocusAndMetering(action)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            true
        }
    }

    /**
     * 显示聚焦view
     */
    private fun showFocusView(x: Int, y: Int) {
        val popupWindow = PopupWindow(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val imageView = ImageView(this)
        imageView.setImageResource(R.drawable.ic_focus)
        popupWindow.contentView = imageView
        popupWindow.showAsDropDown(
            binding.previewView,
            x - dp2px(28),
            y - dp2px(28)
        )
        binding.previewView.postDelayed(popupWindow::dismiss, 600)
        binding.previewView.playSoundEffect(SoundEffectConstants.CLICK)
    }

    /**
     * 拍照存储至内置存储空间
     */
    private fun takePictureInternal() {
        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.MediaColumns.DISPLAY_NAME,
            System.currentTimeMillis().toString()
        )
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        mImageCapture?.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    toast(outputFileResults.savedUri.toString())
                }

                override fun onError(exception: ImageCaptureException) {
                    toast(exception.message)
                }

            })
    }

    @SuppressLint("RestrictedApi")
    private fun recordVideo() {
        if (isRecording) {
            stopRecording()
            return
        }

        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.MediaColumns.DISPLAY_NAME,
            System.currentTimeMillis().toString()
        )
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")

        try {
            isRecording = true
            mVideoCapture?.startRecording(VideoCapture.OutputFileOptions.Builder(
                contentResolver,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build(),
                ContextCompat.getMainExecutor(this),
                object : VideoCapture.OnVideoSavedCallback {
                    override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                        toast(outputFileResults.savedUri.toString())
                    }

                    override fun onError(
                        videoCaptureError: Int,
                        message: String,
                        cause: Throwable?
                    ) {
                        toast(message)
                    }

                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun stopRecording() {
        if (isRecording) {
            isRecording = false
            mVideoCapture?.stopRecording()
        }
    }


}