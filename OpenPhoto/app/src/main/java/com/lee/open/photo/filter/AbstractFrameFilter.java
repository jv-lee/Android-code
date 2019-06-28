package com.lee.open.photo.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.lee.open.photo.utils.OpenGLUtils;


/**
 * @author jv.lee
 * @date 2019-06-22
 * @description 主要是获取摄像头数据 并且创建FBO 在FBO中添加特效
 */
public abstract class AbstractFrameFilter extends AbstractFilter {

    /**
     * FBO int类型
     */
    int[] mFrameBuffer;
    int[] mFrameBufferTextures;

    public AbstractFrameFilter(Context context, int vertexShaderId, int fragmentShaderId) {
        super(context, vertexShaderId, fragmentShaderId);
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
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mWidth, mHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0], 0);


        //解除绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

}
