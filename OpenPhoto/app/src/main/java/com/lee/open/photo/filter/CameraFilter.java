package com.lee.open.photo.filter;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.lee.open.photo.R;
import com.lee.open.photo.utils.OpenGLUtils;


/**
 * @author jv.lee
 * @date 2019-06-22
 * @description 主要是获取摄像头数据 并且创建FBO 在FBO中添加特效
 */
public class CameraFilter extends AbstractFilter {

    /**
     * FBO int类型
     */
    int[] mFrameBuffer;
    int[] mFrameBufferTextures;
    float[] mMatrix;

    public CameraFilter(Context context) {
        super(context, R.raw.camera_vertex, R.raw.camera_frag);
    }

    @Override
    protected void initCoordinate() {
        mTextureBuffer.clear();
        //摄像头颠倒 原始坐标 摄像头是颠倒的（90度） + 镜像
//        float[] TEXTURE = {
//                0.0f, 0.0f,
//                1.0f, 0.0f,
//                0.0f, 1.0f,
//                1.0f, 1.0f
//        };
        float[] TEXTURE = {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f
        };
        /**
         * 修复摄像头旋转
         */
        mTextureBuffer.put(TEXTURE);
    }

    @Override
    public void onReady(int width, int height) {
        super.onReady(width, height);
        mFrameBuffer = new int[1];
        //生成纹理 FBO
        GLES20.glGenFramebuffers(1, mFrameBuffer, 0);

        //实列化一个纹理 -> 纹理和FBO绑定纹理操作
        mFrameBufferTextures = new int[1];
        OpenGLUtils.glGenTextures(mFrameBufferTextures);

        //将纹理和FBO绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0]);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer[0]);

        //设置纹理显示 宽度高度等参数
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mWidth, mHeight, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        //        将纹理 与FBO联系
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D,
                mFrameBufferTextures[0], 0);


        //解除绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);
    }


    @Override
    public int onDrawFrame(int textureId) {
        //设置显示窗口
        GLES20.glViewport(0, 0, mWidth, mHeight);
        //不调用的话就是默认的操作glsurfaceview中的纹理了。显示到屏幕上了
        //这里我们还只是把它画到fbo中(缓存)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer[0]);
        //使用着色器
        GLES20.glUseProgram(mProgram);
        mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        mTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        //变换矩阵
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mMatrix, 0);

        //激活图层
        GLES20.glActiveTexture(GLES20.GL_TEXTURE);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,textureId);

        GLES20.glUniform1i(vTexture, 0);
        //绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        //  解绑
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);
        return mFrameBufferTextures[0];
    }

    public void setMatrix(float[] mtx) {
        mMatrix = mtx;
    }


}
