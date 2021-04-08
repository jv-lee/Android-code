package com.lee.camerax.manager

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.util.Size
import android.view.SoundEffectConstants
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.camera.core.*
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.camera.extensions.BeautyPreviewExtender
import androidx.camera.extensions.NightImageCaptureExtender
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.lee.camerax.R

/**
 * @author jv.lee
 * @date 2021/4/2
 * @description
 */
class CameraXManager {

    companion object {
        const val MIME_TYPE_IMAGE = "image/jpg"
        const val MIME_TYPE_VIDEO = "video/mp4"
    }

    private lateinit var mContext: Context
    private lateinit var mLifecycleOwner: LifecycleOwner
    private lateinit var mPreviewView: PreviewView
    private lateinit var mCameraProvider: ProcessCameraProvider

    //相机实例
    private var mCamera: Camera? = null

    //预览
    private var mPreview: Preview? = null

    //拍照
    private var mImageCapture: ImageCapture? = null

    //图像分析
    private var mImageAnalysis: ImageAnalysis? = null

    //录制
    private var mVideoCapture: VideoCapture? = null

    private var isBack = true
    private var isVideo = false
    private var isRecording = false
    private var isAnalyzing = false

    @SuppressLint("RestrictedApi")
    @RequiresPermission(Manifest.permission.CAMERA)
    fun initCamera(previewView: PreviewView, lifecycleOwner: LifecycleOwner) {
        mContext = previewView.context
        mLifecycleOwner = lifecycleOwner
        mPreviewView = previewView
        ProcessCameraProvider.getInstance(previewView.context).let { processCameraProvider ->
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
            }, CameraXExecutors.mainThreadExecutor())
        }
    }

    /**
     * 切换摄像头
     */
    fun switchCamera() {
        isBack = !isBack
        bindPreview(mCameraProvider, mPreviewView)
    }

    /**
     * 切换录像模式
     */
    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun switchVideo(enable: Boolean) {
        stopRecording()
        isVideo = enable
        bindPreview(mCameraProvider, mPreviewView)
    }

    /**
     * 摄像头是否切换为摄像模式
     */
    fun isVideoMode() = isVideo

    /**
     * 拍照存储至内置存储空间
     */
    @SuppressLint("RestrictedApi")
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun takePicture(callback: ImageCapture.OnImageSavedCallback) {
        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.MediaColumns.DISPLAY_NAME,
            System.currentTimeMillis().toString()
        )
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE_IMAGE)
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
            mContext.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        mImageCapture?.takePicture(
            outputFileOptions,
            CameraXExecutors.mainThreadExecutor(),
            callback
        )
    }

    @SuppressLint("RestrictedApi")
    fun analyzeImage(analyzer: ImageAnalysis.Analyzer) {
        if (isVideo) return

        if (!isAnalyzing) {
            mImageAnalysis?.setAnalyzer(CameraXExecutors.ioExecutor(), analyzer)
        } else {
            mImageAnalysis?.clearAnalyzer()
        }

        isAnalyzing = !isAnalyzing
    }

    /**
     * 开始视频录制
     */
    @SuppressLint("RestrictedApi")
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun startRecord(callback: VideoCapture.OnVideoSavedCallback) {
        if (isRecording) {
            stopRecording()
            return
        }

        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.MediaColumns.DISPLAY_NAME,
            System.currentTimeMillis().toString()
        )
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE_VIDEO)

        try {
            isRecording = true
            mVideoCapture?.startRecording(
                VideoCapture.OutputFileOptions.Builder(
                    mContext.contentResolver,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ).build(),
                CameraXExecutors.mainThreadExecutor(), callback
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 结束录制 初始化状态
     */
    @SuppressLint("RestrictedApi")
    fun stopRecording() {
        if (isRecording) {
            isRecording = false
            mVideoCapture?.stopRecording()
        }
    }

    /**
     * 绑定预览视图
     */
    @SuppressLint("RestrictedApi")
    private fun bindPreview(
        cameraProvider: ProcessCameraProvider,
        previewView: PreviewView
    ) {
        //设置前置/后置摄像头
        val cameraSelector =
            if (isBack) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

        //创建预览配置
        val previewBuilder = Preview.Builder()
        setPreviewExtender(previewBuilder, cameraSelector)
        mPreview = previewBuilder.build()

        //创建拍照配置
        val imageCaptureBuilder = ImageCapture
            .Builder()
            .setTargetRotation(previewView.display.rotation)
        setCaptureExtender(imageCaptureBuilder, cameraSelector)
        mImageCapture = imageCaptureBuilder.build()

        //创建图像分析
        mImageAnalysis = ImageAnalysis.Builder()
            .setTargetRotation(previewView.display.rotation)
            .setTargetResolution(
                Size(
                    mContext.resources.displayMetrics.widthPixels,
                    mContext.resources.displayMetrics.heightPixels
                )
            )
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        //创建录像配置
        mVideoCapture = VideoCapture.Builder()
            .setTargetRotation(previewView.display.rotation)
            .setVideoFrameRate(25)
            .setBitRate(3 * 1024 * 1024)
            .build()

        //清除所有绑定
        cameraProvider.unbindAll()

        //创建相机
        mCamera = if (isVideo) {
            cameraProvider.bindToLifecycle(
                mLifecycleOwner,
                cameraSelector,
                mPreview,
                mVideoCapture
            )
        } else {
            cameraProvider.bindToLifecycle(
                mLifecycleOwner,
                cameraSelector,
                mPreview,
                mImageCapture,
                mImageAnalysis
            )
        }

        //设置视图
        mPreview?.setSurfaceProvider(previewView.surfaceProvider)
    }

    /**
     * 预览视图触摸事件 提供聚焦功能
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun bindTouchPreview() {
        mPreviewView.setOnTouchListener { v, event ->
            val action = FocusMeteringAction.Builder(
                mPreviewView.meteringPointFactory.createPoint(event.x, event.y)
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
        val imageView = ImageView(mPreviewView.context)
        imageView.setImageResource(R.drawable.ic_focus)
        popupWindow.contentView = imageView
        popupWindow.showAsDropDown(
            mPreviewView,
            x - dp2px(18),
            y - dp2px(18)
        )
        mPreviewView.postDelayed(popupWindow::dismiss, 600)
        mPreviewView.playSoundEffect(SoundEffectConstants.CLICK)
    }

    /**
     * 设置预览滤镜
     */
    private fun setPreviewExtender(builder: Preview.Builder, cameraSelector: CameraSelector) {
        val beautyPreviewExtender = BeautyPreviewExtender.create(builder)
        if (beautyPreviewExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            toast("beauty preview extension enable")
            beautyPreviewExtender.enableExtension(cameraSelector)
        } else {
            toast("beauty preview extension not available")
        }
    }

    /**
     * 设置拍照滤镜
     */
    private fun setCaptureExtender(builder: ImageCapture.Builder, cameraSelector: CameraSelector) {
        val nightImageCaptureExtender =
            NightImageCaptureExtender.create(builder)
        if (nightImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            toast("night capture extension enable")
            nightImageCaptureExtender.enableExtension(cameraSelector)
        } else {
            toast("night capture extension not available")
        }
    }

    private fun dp2px(value: Int): Int {
        val scale = mPreviewView.context.resources.displayMetrics.density
        return (value * scale + 0.5f).toInt()
    }

    private fun toast(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

}