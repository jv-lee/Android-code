package com.examples.opengl.widget;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.View;

import com.examples.opengl.utils.CameraHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author jv.lee
 * @date 2019/6/20.
 * description：
 */
public class PhotoRender implements GLSurfaceView.Renderer , SurfaceTexture.OnFrameAvailableListener {

    private CameraHelper mCameraHelper;
    private PhotoView mView;
    private SurfaceTexture mSurfaceTexture;
    /**
     * 纹理ID
     */
    private int[] mTextures;

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
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mView.requestRender();
    }
}
