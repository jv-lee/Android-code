package com.lee.open.photo.filter;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.lee.open.photo.R;

/**
 * @author jv.lee
 * @date 2019-06-29
 * @description 美颜滤镜
 */
public class BeautyFilter extends AbstractFrameFilter {

    private int width;
    private int height;

    public BeautyFilter(Context context) {
        super(context, R.raw.base_vertex, R.raw.beauty_frag);
        width = GLES20.glGetUniformLocation(mProgram, "width");
        height = GLES20.glGetUniformLocation(mProgram, "height");
    }

    @Override
    public int onDrawFrame(int textureId) {
        //设置显示窗口
        GLES20.glViewport(0,0,mWidth,mHeight);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer[0]);

        //使用着色器
        GLES20.glUseProgram(mProgram);
        //设置着色器参数
        GLES20.glUniform1i(width,mWidth);
        GLES20.glUniform1i(height,mHeight);
        mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(vPosition,2,GLES20.GL_FLOAT,false,0,mVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        mTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(vCoord,2,GLES20.GL_FLOAT,false,0,mTextureBuffer);
        GLES20.glEnableVertexAttribArray(vCoord);

        //激活图层
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);

        //绘制
        GLES20.glUniform1i(vTexture,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

        //解除绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);

        return mFrameBufferTextures[0];
    }

    @Override
    protected void initCoordinate() {
        mTextureBuffer.clear();
        float[] TEXTURE = {
                0.0f, 0.0f,
                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f
        };
        mTextureBuffer.put(TEXTURE);
    }

    @Override
    public void onReady(int width, int height) {
        super.onReady(width, height);
    }
}
