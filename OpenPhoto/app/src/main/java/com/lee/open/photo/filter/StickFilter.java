package com.lee.open.photo.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.lee.open.photo.R;
import com.lee.open.photo.face.Face;
import com.lee.open.photo.utils.OpenGLUtils;

/**
 * @author jv.lee
 * @date 2019-06-29
 * @description 贴图效果滤镜
 */
public class StickFilter extends AbstractFrameFilter {
    private Bitmap mBitmap;
    private int[] mTextureId;
    private Face mFace;

    public StickFilter(Context context) {
        super(context, R.raw.base_vertex, R.raw.base_frag);
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
    }

    @Override
    protected void initCoordinate() {

    }

    @Override
    public void onReady(int width, int height) {
        super.onReady(width, height);
        mTextureId = new int[1];

        //在GPU开辟内存加载bitmap  - 纹理ID
        OpenGLUtils.glGenTextures(mTextureId);

        //GPU绑定纹理ID
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId[0]);

        //将bitmap与纹理ID绑定起来
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);

        //解除绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public int onDrawFrame(int textureId) {
        if (mFace == null) {
            return textureId;
        }

        GLES20.glViewport(0, 0, mWidth, mHeight);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer[0]);
        GLES20.glUseProgram(mProgram);
        mVertexBuffer.position(0);

        //获取GPU变量 并设置开关
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        //激活图层
        GLES20.glActiveTexture(GLES20.GL_TEXTURE);
        //绑定图层id
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(vTexture, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        //上一个滤镜 绘制屏幕
        onDrawStick();
        return mFrameBufferTextures[0];
    }

    private void onDrawStick() {
        //开启图片混合模式
        GLES20.glEnable(GLES20.GL_BLEND);
        //GPU一次性渲染 bitmap
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        //获取图形坐标 定位人脸位置
        float x = mFace.faceRects[0] / mFace.imgWidth * mWidth;
        float y = mFace.faceRects[1] / mFace.imgHeight * mHeight;

        //动态绘制  获取动态脸部贴图 x y 坐标，  根据脸部宽度 / 图片宽图 * 屏幕宽度  = 动态获取的贴图宽度   判断远近
        GLES20.glViewport((int) x, (int) y-mBitmap.getHeight()/2, (int) ((float) (mFace.width / mFace.imgWidth * mWidth)), mBitmap.getHeight());

        //与onDrawFrame
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer[0]);
        GLES20.glUseProgram(mProgram);
        mVertexBuffer.position(0);

        //获取GPU变量 并设置开关
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(vPosition);

        //激活图层
        GLES20.glActiveTexture(GLES20.GL_TEXTURE);
        //绑定图层id
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId[0]);
        GLES20.glUniform1i(vTexture, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0 );

        //关闭图形混合模式
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    public void setFace(Face mFace) {
        this.mFace = mFace;
    }
}
