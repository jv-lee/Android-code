package com.lee.camerax

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.ImageFormat
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.SoundEffectConstants
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.camera.core.*
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.camera.core.internal.utils.ImageUtil
import androidx.camera.extensions.BeautyPreviewExtender
import androidx.camera.extensions.NightImageCaptureExtender
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import com.lee.camerax.base.BaseActivity
import com.lee.camerax.base.dp2px
import com.lee.camerax.databinding.ActivityMainBinding
import java.lang.Boolean
import java.util.*


/**
 * @author jv.lee
 * @date 2021/3/23
 * @description CameraX 示例
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var mCameraProvider: ProcessCameraProvider? = null
    private var mCamera: Camera? = null

    //预览
    private var mPreview: Preview? = null

    //拍照
    private var mImageCapture: ImageCapture? = null

    //图像分析
    private var mImageAnalysis: ImageAnalysis? = null

    //录制
    private var mVideoCapture: VideoCapture? = null

    //zxing解析
    private val multiFormatReader by lazy { MultiFormatReader() }
    private val qrCodeMultiReader by lazy { QRCodeMultiReader() }

    private var isBack = true
    private var isVideo = false
    private var isRecording = false
    private var isAnalyzing = false

    companion object {
        const val MIME_TYPE_IMAGE = "image/jpg"
        const val MIME_TYPE_VIDEO = "video/mp4"
    }

    override fun bindViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun bindView() {
        initCamera()
        bindSwitchVideo()
        toast("width:${resources.displayMetrics.widthPixels},height:${resources.displayMetrics.heightPixels}")
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

    /**
     * 切换摄像头
     */
    fun onSwitchCamera(view: View) {
        mCameraProvider?.let {
            isBack = !isBack
            bindPreview(it, binding.previewView)
        }
    }

    /**
     * 拍照/设想
     */
    fun onTakePicture(view: View) {
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, {
            if (isVideo) {
                recordVideo()
            } else {
                takePictureInternal()
            }
        }, { toast(it) })
    }

    @SuppressLint("RestrictedApi")
    fun onAnalyzeGo(view: View) {
        if (isVideo) return

        if (!isAnalyzing) {
            mImageAnalysis?.setAnalyzer(
                CameraXExecutors.ioExecutor(),
                ImageAnalysis.Analyzer {
                    analyzeQRCode(it)
                })
        } else {
            mImageAnalysis?.clearAnalyzer()
        }

        isAnalyzing = !isAnalyzing
    }

    /**
     * 初始化相机
     */
    private fun initCamera() {
        requestPermission(Manifest.permission.CAMERA, {
            setUpCamera(binding.previewView)
        }, { toast(it) })
    }

    /**
     * 设置相机提供者 启动绑定预览
     */
    @SuppressLint("RestrictedApi")
    private fun setUpCamera(previewView: PreviewView) {
        ProcessCameraProvider.getInstance(this).let { processCameraProvider ->
            processCameraProvider.addListener(Runnable {
                try {
                    mCameraProvider = processCameraProvider.get()
                    //绑定预览视图
                    bindPreview(mCameraProvider!!, previewView)
                    //绑定触摸聚焦
                    bindTouchPreview()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, CameraXExecutors.mainThreadExecutor())
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
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels
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
            cameraProvider.bindToLifecycle(this, cameraSelector, mPreview, mVideoCapture)
        } else {
            cameraProvider.bindToLifecycle(
                this,
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
        binding.previewView.setOnTouchListener { v, event ->
            val action = FocusMeteringAction.Builder(
                binding.previewView.meteringPointFactory.createPoint(event.x, event.y)
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
            x - dp2px(18),
            y - dp2px(18)
        )
        binding.previewView.postDelayed(popupWindow::dismiss, 600)
        binding.previewView.playSoundEffect(SoundEffectConstants.CLICK)
    }

    /**
     * 拍照存储至内置存储空间
     */
    @SuppressLint("RestrictedApi")
    private fun takePictureInternal() {
        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.MediaColumns.DISPLAY_NAME,
            System.currentTimeMillis().toString()
        )
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE_IMAGE)
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        mImageCapture?.takePicture(
            outputFileOptions,
            CameraXExecutors.mainThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    toast(outputFileResults.savedUri.toString())
                }

                override fun onError(exception: ImageCaptureException) {
                    toast(exception.message)
                }

            })
    }

    /**
     * 开始视频录制
     */
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
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE_VIDEO)

        try {
            isRecording = true
            mVideoCapture?.startRecording(VideoCapture.OutputFileOptions.Builder(
                contentResolver,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build(),
                CameraXExecutors.mainThreadExecutor(),
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

    /**
     * 结束录制 初始化状态
     */
    @SuppressLint("RestrictedApi")
    private fun stopRecording() {
        if (isRecording) {
            isRecording = false
            mVideoCapture?.stopRecording()
        }
    }

    /**
     * 二维码解析
     */
    private fun analyzeQRCode(image: ImageProxy) {
        val buffer = image.planes[0].buffer
        val data = ByteArray(buffer.remaining())
        buffer.get(data)

        val width = image.width
        val height = image.height
        val source = PlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        val hints: Hashtable<DecodeHintType, Any> = Hashtable<DecodeHintType, Any>()
        hints[DecodeHintType.CHARACTER_SET] = "UTF-8"
        //复杂模式，开启PURE_BARCODE模式,带图片LOGO的解码方案,否则会出现NotFoundException
        hints[DecodeHintType.PURE_BARCODE] = Boolean.TRUE

        val result = try {
//            qrCodeMultiReader.decode(bitmap, hints)
            multiFormatReader.decode(bitmap, hints)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        toast(result?.text)
        image.close()
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

}