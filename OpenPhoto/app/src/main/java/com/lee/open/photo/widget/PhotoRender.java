package com.lee.open.photo.widget;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Environment;


import com.lee.open.photo.face.FaceJni;
import com.lee.open.photo.filter.BeautyFilter;
import com.lee.open.photo.filter.BigEyeFilter;
import com.lee.open.photo.filter.CameraFilter;
import com.lee.open.photo.filter.ScreenFilter;
import com.lee.open.photo.filter.StickFilter;
import com.lee.open.photo.utils.CameraHelper;
import com.lee.open.photo.utils.OpenGLUtils;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author jv.lee
 * @date 2019/6/20.
 * description：
 */
public class PhotoRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener, Camera.PreviewCallback {

    private CameraHelper mCameraHelper;
    private PhotoView mView;
    private SurfaceTexture mSurfaceTexture;
    /**
     * 纹理ID
     */
    private int[] mTextures;
    private float[] mtx = new float[16];

    private int mWidth;
    private int mHeight;

    private CameraFilter mCameraFilter;
    private ScreenFilter mScreenFilter;
    /**
     * 大眼睛滤镜
     */
    private BigEyeFilter mBigEyeFilter;
    /**
     * 贴图滤镜
     */
    private StickFilter mStickFilter;
    /**
     * 美颜滤镜
     */
    private BeautyFilter mBeautyFilter;

    private FaceJni faceJni;
    File lbpcascade_frontalface = new File(Environment.getExternalStorageDirectory(), "lbpcascade_frontalface.xml");
    File seeta_fa_v1 = new File(Environment.getExternalStorageDirectory(), "seeta_fa_v1.1.bin");


    public PhotoRender(PhotoView view) {
        mView = view;
        //拷贝 模型
        OpenGLUtils.copyAssets2SdCard(mView.getContext(), "lbpcascade_frontalface.xml",
                lbpcascade_frontalface.getAbsolutePath());
        OpenGLUtils.copyAssets2SdCard(mView.getContext(), "seeta_fa_v1.1.bin",
                seeta_fa_v1.getAbsolutePath());
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //打开摄像头
        mCameraHelper = new CameraHelper(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mTextures = new int[1];
        //入参出参对象
        GLES20.glGenTextures(mTextures.length, mTextures, 0);
        mSurfaceTexture = new SurfaceTexture(mTextures[0]);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        //获取摄像头矩阵 使摄像头数据不会变形 顶点矩阵
        mSurfaceTexture.getTransformMatrix(mtx);
        mCameraFilter = new CameraFilter(mView.getContext());
        mScreenFilter = new ScreenFilter(mView.getContext());
        mBigEyeFilter = new BigEyeFilter(mView.getContext());
        mStickFilter = new StickFilter(mView.getContext());
//        mBeautyFilter = new BeautyFilter(mView.getContext());
        mCameraFilter.setMatrix(mtx);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
        mCameraHelper.startPreview(mSurfaceTexture);
        mCameraHelper.setPreviewCallback(this);
        mCameraFilter.onReady(width, height);
        mScreenFilter.onReady(width, height);
        mBigEyeFilter.onReady(width, height);
        mStickFilter.onReady(width, height);
        faceJni = new FaceJni(lbpcascade_frontalface.getAbsolutePath(), seeta_fa_v1.getAbsolutePath(), mCameraHelper);
        faceJni.startTrack();
    }

    /**
     * @param gl
     * @description 摄像头每一帧数据回调该方法
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        //指定清空的颜色
        GLES20.glClearColor(0, 0, 0, 0);
        //执行清空
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(mtx);
        mCameraFilter.setMatrix(mtx);

        //传入人脸数据
        mBigEyeFilter.setFace(faceJni.getFace());
        mStickFilter.setFace(faceJni.getFace());

        int drawId;
        //传入纹理 设置滤镜
        drawId = mCameraFilter.onDrawFrame(mTextures[0]);
        drawId = mBigEyeFilter.onDrawFrame(drawId);
        drawId = mStickFilter.onDrawFrame(drawId);
        if (mBeautyFilter != null) {
            drawId = mBeautyFilter.onDrawFrame(drawId);
        }
        mScreenFilter.onDrawFrame(drawId);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mView.requestRender();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        //回调nv21数据
        faceJni.detector(data);
    }

    public void enableBeauty(final boolean isChecked) {
        //从GL线程回调到主线程
        mView.queueEvent(new Runnable() {
            @Override
            public void run() {
                if (isChecked) {
                    mBeautyFilter = new BeautyFilter(mView.getContext());
                    mBeautyFilter.onReady(mWidth, mHeight);
                } else {
                    mBeautyFilter = null;
                }
            }
        });

    }
}
