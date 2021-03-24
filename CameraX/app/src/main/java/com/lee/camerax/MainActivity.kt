package com.lee.camerax

import android.Manifest
import android.annotation.SuppressLint
import android.view.SoundEffectConstants
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.lee.camerax.base.BaseActivity
import com.lee.camerax.databinding.ActivityMainBinding
import org.jetbrains.annotations.NotNull

/**
 * @author jv.lee
 * @date 2021/3/23
 * @description CameraX 示例
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var mCameraProvider: ProcessCameraProvider? = null
    private var mPreview: Preview? = null
    private var mCamera: Camera? = null

    private var isBack = true

    override fun bindViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun bindView() {
        initPreview()
    }

    fun onSwitchCamera(view: View) {
        mCameraProvider?.let {
            isBack = !isBack
            bindPreview(it, binding.previewView)
        }
    }

    private fun initPreview() {
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
    private fun bindPreview(
        @NotNull cameraProvider: ProcessCameraProvider?,
        previewView: PreviewView
    ) {
        cameraProvider ?: return
        mPreview = Preview.Builder().build()
        val cameraSelector =
            if (isBack) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA
        cameraProvider.unbindAll()
        mCamera = cameraProvider.bindToLifecycle(this, cameraSelector, mPreview)
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
        popupWindow.showAsDropDown(binding.previewView, x, y)
        binding.previewView.postDelayed(popupWindow::dismiss, 600)
        binding.previewView.playSoundEffect(SoundEffectConstants.CLICK)
    }

}