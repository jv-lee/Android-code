package com.lee.opencv.face.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import com.lee.opencv.face.widget.AutoFitTextureView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author jv.lee
 * @date 2019/6/26.
 * @description Camera2API使用工具
 */
public class CameraTool {

    private static String TAG = "CameraTool";

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
    private int mCameraId = CameraCharacteristics.LENS_FACING_FRONT;

    /**
     * 相机预览的{@link android.util.Size}。
     */
    private Size mPreviewSize;

    /**
     * {@link CaptureRequest.Builder}用于相机预览
     */
    private CaptureRequest.Builder mPreviewRequestBuilder;

    /**
     * {@link #mPreviewRequestBuilder}生成的{@link CaptureRequest}
     */
    private CaptureRequest mPreviewRequest;

    /**
     * A {@link Semaphore} 在关闭相机之前阻止应用程序退出.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private Surface mSurface;

    /**
     * 当前相机设备是否支持Flash。
     */
    private boolean mFlashSupported;

    /**
     * 相机传感器的方向
     */
    private int mSensorOrientation;

    /**
     * Camera2 API保证的最大预览宽度
     */
    private static final int MAX_PREVIEW_WIDTH = 1920;

    /**
     * Camera2 API保证的最大预览高度
     */
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    /**
     * 用于拍照的相机状态的当前状态。
     *
     * @see #mCaptureCallback
     */
    private int mState = STATE_PREVIEW;

    /**
     * Camera state: 显示相机预览。
     */
    private static final int STATE_PREVIEW = 0;

    /**
     * Camera state: 等待焦点被锁定。
     */
    private static final int STATE_WAITING_LOCK = 1;

    /**
     * Camera state: 等待暴露是预先捕获状态。
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;

    /**
     * Camera state: 等待暴露状态不是预先捕获的东西。
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;

    /**
     * Camera state: 照片拍摄成功。
     */
    private static final int STATE_PICTURE_TAKEN = 4;

    /**
     * 显示图像宽度
     */
    private int mWidth;

    /**
     * 显示图像高度
     */
    private int mHeight;

    /**
     * 照片存放目录
     */
    File mFile;

    /**
     * {@link PictureCallback}图像回调接口
     */
    private static PictureCallback mPictureCallback;

    /**
     * 这是{@link ImageReader}的回调对象。 a时会调用“onImageAvailable”
     * 静止图像已准备好保存。
     */
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            File file = new File(mFile == null ? Environment.getExternalStorageDirectory() : mFile,
                    System.currentTimeMillis() + ".jpg");
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), file));
        }

    };

    /**
     * 这是{@link ImageReader}的回调对象。 a时会调用“onImageAvailable”
     * 图像预览原始数据。
     */
    private ImageReader.OnImageAvailableListener mOnPreviewAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Log.i(TAG, "原始数据预览");
        }
    };

    /**
     * {@link TextureView.SurfaceTextureListener}处理a上的几个生命周期事件
     * {@link TextureView}.
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
            if (ThreadTool.getInstance().getExecutor().getActiveCount() > 0) {
                return;
            }
            ThreadTool.getInstance().addTask(new Runnable() {
                @Override
                public void run() {
                    if (mTextureView != null) {
                        if (mTextureView.getBitmap() != null) {
                            Bitmap bitmap = mTextureView.getBitmap();
                            final byte[] nv21 = Utils.getNV21(bitmap.getWidth(), bitmap.getHeight(), bitmap);
                            onPreviewFrame(nv21);
                        }
                    }
                }
            });

        }

    };

    /**
     * {@link CameraDevice.StateCallback}在{@link CameraDevice}更改其状态时被调用。
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            if (null != mActivity.get()) {
                mActivity.get().finish();
            }
        }

    };

    /**
     * 一个{@link CameraCaptureSession.CaptureCallback}，用于处理与JPEG捕获相关的事件。
     */
    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {
                    // 当相机预览正常工作时，我们无事可做。
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        Log.i(TAG, "afState == null");
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE在某些设备上可以为null
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    } else {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE在某些设备上可以为null
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE在某些设备上可以为null
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
                default:
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }

    };

    public CameraTool(Activity activity, AutoFitTextureView textureView) {
        mActivity = new WeakReference<>(activity);
        mTextureView = textureView;
    }

    /**
     * 启动后台线程及其{@link Handler}。
     */
    public void startBackgroundThread() {
        //创建handlerThread 使子线程创建的handler 可以自动调用 Looper轮询机制 Looper.prepare() Looper.loop()
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * 停止后台线程及其{@link Handler}。
     */
    public void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启相机预览
     */
    public void startPreview() {
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    /**
     * 关闭当前的{@link CameraDevice}。
     */
    public void stopPreview() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * 切换摄像头。
     */
    public void switchCamera() {
        if (mCameraId == CameraCharacteristics.LENS_FACING_FRONT) {
            mCameraId = CameraCharacteristics.LENS_FACING_BACK;
        } else {
            mCameraId = CameraCharacteristics.LENS_FACING_FRONT;
        }
        stopPreview();
        startPreview();
    }

    /**
     * 启动静止图像捕获。
     */
    public void takePicture() {
        lockFocus();
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getCameraId() {
        return mCameraId;
    }

    /**
     * 设置图像回调接口
     *
     * @param pictureCallback 图像回调接口
     */
    public void setPictureCallback(PictureCallback pictureCallback) {
        mPictureCallback = pictureCallback;
    }


    /**
     * 打开{@link ＃mCameraId}指定的摄像头。
     *
     * @param width
     * @param height
     */
    private void openCamera(int width, int height) {
        if (ContextCompat.checkSelfPermission(mActivity.get(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            showToast("Request Camera Permission");
            return;
        }
        setUpCameraOutputs(width, height);
        configureTransform(width, height);
        CameraManager manager = (CameraManager) mActivity.get().getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("超时等待锁定相机开启.");
            }
            manager.openCamera(String.valueOf(mCameraId), mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("在试图锁定相机打开时中断.", e);
        }
    }

    /**
     * 为相机预览创建一个新的{@link CameraCaptureSession}。
     */
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;

            //我们将默认缓冲区的大小配置为我们想要的相机预览的大小。
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            //这是我们开始预览所需的输出Surface。
            mSurface = new Surface(texture);
            onSurfaceChange(mSurface, mWidth, mHeight);

            //我们使用输出Surface设置CaptureRequest.Builder。
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
//            mPreviewRequestBuilder.addTarget(mPreviewReader.getSurface());
            mPreviewRequestBuilder.addTarget(mSurface);

            //在这里，我们为相机预览创建一个CameraCaptureSession。 mPreviewReader.getSurface()
            mCameraDevice.createCaptureSession(Arrays.asList(mSurface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            //相机已经关闭
                            if (null == mCameraDevice) {
                                return;
                            }

                            //会话准备就绪后，我们开始显示预览。
                            mCaptureSession = cameraCaptureSession;
                            try {
                                //对于相机预览，自动对焦应该是连续的。
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                //必要时自动启用Flash。
                                setAutoFlash(mPreviewRequestBuilder);
                                // 设置预览画面成像角度Orientation
                                int rotation = mActivity.get().getWindowManager().getDefaultDisplay().getRotation();
                                mPreviewRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

                                //最后，我们开始显示相机预览。
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                            showToast("Failed");
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将焦点锁定为静止图像捕获的第一步。
     */
    private void lockFocus() {
        try {
            // 这是告诉相机锁定焦点的方法。
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);
            // 告诉#mCaptureCallback等待锁定。
            mState = STATE_WAITING_LOCK;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将必要的{@link android.graphics.Matrix}转换配置为`mTextureView`。
     * 在确定相机预览尺寸后应调用此方法
     * setUpCameraOutputs以及`mTextureView`的大小是固定的。
     *
     * @param viewWidth`mTextureView`的宽度
     * @param viewHeight`mTextureView`的高度
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = mActivity.get();
        if (null == mTextureView || null == mPreviewSize || null == activity) {
            return;
        }

        //设置初始宽高
        mWidth = viewWidth;
        mHeight = viewHeight;
        //将textureView 宽高改变后的参数回调
//        onSurfaceChange(new Surface(mTextureView.getSurfaceTexture()), viewWidth, viewHeight);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    /**
     * 设置与摄像头相关的成员变量。
     *
     * @param width  摄像机预览的可用大小宽度
     * @param height 相机预览的可用尺寸高度
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private void setUpCameraOutputs(int width, int height) {
        Activity activity = mActivity.get();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(String.valueOf(mCameraId));
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            //对于静态图像捕获，我们使用最大可用大小。
            Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
            mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, 2);
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);

            Size largest2 = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.YUV_420_888)), new CompareSizesByArea());
            mPreviewReader = ImageReader.newInstance(largest2.getWidth(), largest2.getHeight(), ImageFormat.YUV_420_888, 2);
            mPreviewReader.setOnImageAvailableListener(mOnPreviewAvailableListener, null);

            //找出我们是否需要交换尺寸以获得相对于传感器的预览尺寸
            // 坐标.
            int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            //noinspection ConstantConditions
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            boolean swappedDimensions = false;
            switch (displayRotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_180:
                    if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                        swappedDimensions = true;
                    }
                    break;
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                        swappedDimensions = true;
                    }
                    break;
                default:
                    Log.e(TAG, "Display rotation is invalid: " + displayRotation);
            }

            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            int rotatedPreviewWidth = width;
            int rotatedPreviewHeight = height;
            int maxPreviewWidth = displaySize.x;
            int maxPreviewHeight = displaySize.y;

            if (swappedDimensions) {
                rotatedPreviewWidth = height;
                rotatedPreviewHeight = width;
                maxPreviewWidth = displaySize.y;
                maxPreviewHeight = displaySize.x;
            }

            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                maxPreviewWidth = MAX_PREVIEW_WIDTH;
            }

            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                maxPreviewHeight = MAX_PREVIEW_HEIGHT;
            }

            //危险，W.R。 尝试使用太大的预览尺寸可能会超出相机
            //总线'带宽限制，导致华丽的预览，但存储
            //垃圾捕获数据
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                    maxPreviewHeight, largest);

            //我们将TextureView的宽高比与我们选择的预览大小相匹配。
            int orientation = mActivity.get().getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else {
                mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }

            //检查是否支持闪光灯。
            Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            mFlashSupported = available == null ? false : available;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            //当使用Camera2API但不支持时，会抛出NPE
            //此代码运行的设备。
            showToast("相机错误");
        }
    }

    /**
     * 给定摄像机支持的{@code Size} {@code choices}，选择最小的一个
     * 至少与相应的纹理视图大小一样大，并且至多与纹理视图大小一样大
     * 各自的最大尺寸，其宽高比与指定值匹配。如果这么大
     * 不存在，选择最大的最大尺寸，相应的最大尺寸，
     * 和宽高比与指定值匹配。
     *
     * @param choices           相机支持预期输出的尺寸列表
     *                          上课
     * @param textureViewWidth  纹理视图相对于传感器坐标的宽度
     * @param textureViewHeight 纹理视图相对于传感器坐标的高度
     * @param maxWidth          可以选择的最大宽度
     * @param maxHeight         可以选择的最大高度
     * @param aspectRatio       纵横比
     * @return 最佳{@code Size}，如果没有足够大，则为任意一个
     */
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        //收集至少与预览曲面一样大的支持分辨率
        List<Size> bigEnough = new ArrayList<>();
        //收集小于预览曲面的支持的分辨率
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        //挑选那些足够大的那些。 如果没有足够的人，请选择
        //最大的那些不够大。
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    /**
     * 让相机打开flash功能
     *
     * @param requestBuilder 拍照构建
     */
    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    /**
     * 运行预捕获序列以捕获静止图像。 应该在调用此方法时调用
     * 我们从{@link #lockFocus（）}获得{@link #mCaptureCallback}的回复。
     */
    private void runPrecaptureSequence() {
        try {
            // 这是告诉相机触发的方法。
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // 告诉#mCaptureCallback等待设置预捕获序列。
            mState = STATE_WAITING_PRECAPTURE;
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 拍摄静止图片。 当我们得到响应时，应该调用此方法
     * {@link #mCaptureCallback} 来自两者 {@link #lockFocus()}.
     */
    private void captureStillPicture() {
        try {
            final Activity activity = mActivity.get();
            if (null == activity || null == mCameraDevice) {
                return;
            }
            // 这是我们用来拍照的CaptureRequest.Builder。
            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            // 使用与预览相同的AE和AF模式。
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(captureBuilder);

            // Orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    unlockFocus();
                }
            };

            mCaptureSession.stopRepeating();
            mCaptureSession.abortCaptures();
            mCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从指定的屏幕旋转中检索JPEG方向。
     *
     * @param rotation 屏幕旋转方向
     * @return JPEG方向（0,90,270和360之一）
     */
    private int getOrientation(int rotation) {
        //对于大多数设备，传感器方向为90，对于某些设备，传感器方向为270（例如，Nexus 5X）
        //我们必须考虑到这一点并正确旋转JPEG。
        //对于方向为90的设备，我们只需从ORIENTATIONS返回我们的映射。
        //对于方向为270的设备，我们需要将JPEG旋转180度。
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    /**
     * 解锁焦点。 应该在静止图像捕获序列时调用此方法
     * 结束.
     */
    private void unlockFocus() {
        try {
            // 重置自动对焦触发器
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            setAutoFlash(mPreviewRequestBuilder);
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);
            // 在此之后，相机将返回正常的预览状态。
            mState = STATE_PREVIEW;
            mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在UI线程上显示{@link Toast}。
     *
     * @param text 要显示的消息
     */
    private void showToast(final String text) {
        final Activity activity = mActivity.get();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /**
     * 根据区域比较两个{@code Size}。
     */
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            //我们在这里投射以确保乘法不会溢出
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }


    /**
     * 将JPEG {@link Image}保存到指定的{@link File}中。
     */
    private static class ImageSaver implements Runnable {

        /**
         * JPEG图像
         */
        private final Image mImage;
        /**
         * 我们将图像保存到的文件。
         */
        private final File mFile;

        ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile);
                output.write(bytes);
                onFileSaveCall(mFile);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private static void onPreviewFrame(byte[] data) {
        if (mPictureCallback != null) {
            mPictureCallback.onPreviewFrame(data);
        }
    }

    /**
     * 回调数据
     *
     * @param file 图像文件
     */
    private static void onFileSaveCall(File file) {
        if (mPictureCallback != null) {
            mPictureCallback.onPicture(file.getAbsolutePath());
        }
    }

    /**
     * 回调加载数据
     *
     * @param surface 绘制层
     * @param width   窗口宽度
     * @param height  窗口高度
     */
    private static void onSurfaceChange(Surface surface, int width, int height) {
        if (mPictureCallback != null) {
            mPictureCallback.onTextureChange(surface, width, height);
        }
    }

    /**
     * {@link PictureCallback} 图像数据回调接口
     */
    public interface PictureCallback {

        /**
         * 返回图像原始数据
         *
         * @param data nv21
         */
        void onPreviewFrame(byte[] data);

        /**
         * 相机拍摄照片后的回调
         *
         * @param path 图像地址
         */
        void onPicture(String path);

        /**
         * {@link TextureView} 窗口改变 数据返回
         *
         * @param surface 绘制层
         * @param width   窗口宽度
         * @param height  窗口高度
         */
        void onTextureChange(Surface surface, int width, int height);
    }

}
