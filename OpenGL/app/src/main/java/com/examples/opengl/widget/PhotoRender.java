package com.examples.opengl.widget;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.examples.opengl.filter.CameraFilter;
import com.examples.opengl.filter.ScreenFilter;
import com.examples.opengl.utils.CameraHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author jv.lee
 * @date 2019/6/20.
 * description：
 */
public class PhotoRender implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private CameraHelper mCameraHelper;
    private PhotoView mView;
    private SurfaceTexture mSurfaceTexture;
    private CameraFilter mCameraFilter;
    private ScreenFilter mScreenFilter;
    /**
     * 纹理ID
     */
    private int[] mTextures;
    private float[] mtx = new float[16];

    public PhotoRender(PhotoView view) {
        mView = view;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //打开摄像头
        mCameraHelper = new CameraHelper(Camera.CameraInfo.CAMERA_FACING_BACK);
        mTextures = new int[1];
        //入参出参对象
        GLES20.glGenTextures(mTextures.length, mTextures, 0);
        mSurfaceTexture = new SurfaceTexture(mTextures[0]);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        //获取摄像头矩阵 使摄像头数据不会变形 顶点矩阵
        mSurfaceTexture.getTransformMatrix(mtx);
        mCameraFilter = new CameraFilter(mView.getContext());
        mScreenFilter = new ScreenFilter(mView.getContext());
        mCameraFilter.setMatrix(mtx);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mCameraHelper.startPreview(mSurfaceTexture);
        mCameraFilter.onReady(width,height);
        mScreenFilter.onReady(width,height);
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

        //传入纹理
        mCameraFilter.onDrawFrame(mTextures[0]);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mView.requestRender();
    }
}
