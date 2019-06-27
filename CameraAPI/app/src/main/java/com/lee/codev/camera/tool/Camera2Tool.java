package com.lee.codev.camera.tool;

import android.app.Activity;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.SparseIntArray;
import android.view.Surface;

import com.lee.codev.camera.widget.AutoFitTextureView;

import java.lang.ref.WeakReference;

/**
 * @author jv.lee
 * @date 2019-06-27
 * @description
 */
public class Camera2Tool {
    private static String TAG = "Camera2Tool";

    /**
     * 从屏幕旋转转换为JPEG方向。
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private WeakReference<Activity> mActivity;

    /**
     * 用于相机预览的{@link AutoFitTextureView}。
     */
    private AutoFitTextureView mTextureView;

    /**
     * 用于运行不应阻止UI的任务的附加线程。
     */
    private HandlerThread mBackgroundThread;

    /**
     * 用于在后台运行任务的{@link Handler}。
     */
    private Handler mBackgroundHandler;

    /**
     * 对已打开的{@link CameraDevice}的引用。
     */
    private CameraDevice mCameraDevice;

    /**
     * 用于相机预览的{@link CameraCaptureSession}。
     */
    private CameraCaptureSession mCaptureSession;

    /**
     * 一个处理静态图像捕获的{@link ImageReader}。
     */
    private ImageReader mImageReader;

    /**
     * 一个处理预览图像原始数据的{@link ImageReader}
     */
    private ImageReader mPreviewReader;

    /**
     * 当前{@link CameraDevice}的ID。
     */
    private int mCameraId = CameraCharacteristics.LENS_FACING_BACK;

}
