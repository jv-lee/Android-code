package com.lee.open.photo.filter;

import android.content.Context;
import android.opengl.GLES20;


import com.lee.open.photo.utils.OpenGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author jv.lee
 * @date 2019/6/20.
 * @description 滤镜基类
 */
public abstract class AbstractFilter {

    /**
     * 顶点着色器
     */
    protected int mVertexShaderId;
    protected FloatBuffer mVertexBuffer;
    /**
     * 片元着色器
     */
    protected int mFragmentShaderId;
    protected FloatBuffer mTextureBuffer;

    protected int vTexture;
    protected int vMatrix;
    protected int vCoord;
    protected int vPosition;
    protected int mProgram;
    protected int mWidth;
    protected int mHeight;

    public AbstractFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        this.mVertexShaderId = vertexShaderId;
        this.mFragmentShaderId = fragmentShaderId;
        //摄像头是2D
        mVertexBuffer = ByteBuffer.allocateDirect(4 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.clear();
        float[] VERTEX = {
                -1.0f, -1.0f,
                1.0f, -1.0f,
                -1.0f, 1.0f,
                1.0f, 1.0f
        };
        mVertexBuffer.put(VERTEX);

        mTextureBuffer = ByteBuffer.allocateDirect(4 * 2 * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mTextureBuffer.clear();
        float[] TEXTURE = {
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f
        };
        mTextureBuffer.put(TEXTURE);
        initilize(context);
        initCoordinate();
    }

    protected abstract void initCoordinate();

    private void initilize(Context context) {
        //获取着色器代码
        String vertexShader = OpenGLUtils.readRawTextFile(context, mVertexShaderId);
        String fragmentShader = OpenGLUtils.readRawTextFile(context, mFragmentShaderId);

        //获取程序
        mProgram = OpenGLUtils.loadProgram(vertexShader, fragmentShader);
        //获取vPosition
        vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        vCoord = GLES20.glGetAttribLocation(mProgram, "vCoord");
        vMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
        vTexture = GLES20.glGetUniformLocation(mProgram, "vTexture");
    }

    public int onDrawFrame(int textureId) {
        //设置显示窗口
        GLES20.glViewport(0, 0, mWidth, mHeight);
        //使用着色器
        GLES20.glUseProgram(mProgram);
        mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        mTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        //激活摄像头 采集数据
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(vTexture, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        return textureId;
    }

    public void onReady(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }
}
