package com.examples.opengl.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.examples.opengl.utils.OpenGLUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

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


    protected int mProgram;
    protected int vTexture;
    protected int vMatrix;
    protected int vCoord;
    protected int vPosition;
    protected int mWidth;
    protected int mHeight;

    public AbstractFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        this.mVertexShaderId = vertexShaderId;
        this.mFragmentShaderId = fragmentShaderId;
        //摄像头是2D
        mVertexBuffer = ByteBuffer.allocateDirect(4 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.clear();
        //java层坐标转换为OpenGL
        float[] VERIEX = {
                -1.0f, -1.0f,
                1.0f, -1.0f,
                -1.0f, 1.0f,
                1.0f, 1.0f
        };
        mVertexBuffer.put(VERIEX);

        mTextureBuffer = ByteBuffer.allocateDirect(4 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureBuffer.clear();
        //java层坐标转换为OpenGL
        float[] TEXTURE = {
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f
        };
        mTextureBuffer.put(VERIEX);
        initilize(context);
    }

    protected abstract void initCoordination();

    private void initilize(Context context) {
        //获取着色器代码
        String vertexShader = OpenGLUtil.readRawTextFile(context, mVertexShaderId);
        String fragmentShader = OpenGLUtil.readRawTextFile(context, mFragmentShaderId);

        //获取程序
        mProgram = OpenGLUtil.loadProgram(vertexShader, fragmentShader);
        //获取vPosition
        vPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        vCoord = GLES20.glGetAttribLocation(mProgram, "vCoord");
        vMatrix = GLES20.glGetAttribLocation(mProgram, "vMatrix");
        vTexture = GLES20.glGetAttribLocation(mProgram, "vTexture");
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
        mWidth = width;
        mHeight = height;
    }
}
