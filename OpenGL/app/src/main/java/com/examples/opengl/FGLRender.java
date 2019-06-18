package com.examples.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.View;

import com.examples.opengl.shape.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author jv.lee
 * @date 2019/6/18.
 * description：
 */
public class FGLRender implements GLSurfaceView.Renderer {
    protected View mView;
    Triangle triangle;

    public FGLRender(View view) {
        mView = view;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //清空颜色
        GLES20.glClearColor(0, 0, 0, 0);
        triangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        triangle.onSurfaceChanged(gl, width, height);
    }

    /**
     * 不断被调用  requestRender == inviladate
     *
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        triangle.onDrawFrame(gl);
    }
}
