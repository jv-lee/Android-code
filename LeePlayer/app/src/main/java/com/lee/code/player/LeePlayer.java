package com.lee.code.player;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author jv.lee
 * @date 2019/6/5.
 * description：
 */
public class LeePlayer implements SurfaceHolder.Callback {

    static {
        System.loadLibrary("player");
    }

    private native void nativePrepare(String dataSource);

    private native void nativeStart();

    private native void nativeSetSurface(Surface surface);

    private SurfaceHolder mSurfaceHolder;
    private String dataSource;

    void setSurfaceView(SurfaceView surfaceView) {
        if (null != mSurfaceHolder) {
            mSurfaceHolder.removeCallback(this);
        }

        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        nativeSetSurface(holder.getSurface());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * native层准备
     */
    void prepare() {
        nativePrepare(dataSource);
    }

    /**
     * 设置数据源路径
     *
     * @param dataSource
     */
    void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 开始播放方法
     */
    public void start() {
        nativeStart();
    }

    /**
     * native层反射调用
     * native层准备完成后调用
     */
    public void onPrepare() {
        if (null != onPrepareListener) {
            onPrepareListener.onPrepared();
        }
    }

    /**
     * native层反射调用
     * native层进度不断回调
     */
    public void onProgress(int progress) {
        if (null != onProgressListener) {
            onProgressListener.onProgress(progress);
        }
    }

    /**
     * native层反射调用
     * native层错误信息回调
     */
    public void onError(int errorCode) {
        if (null != onErrorListener) {
            onErrorListener.onError(errorCode);
        }
    }

    private OnPrepareListener onPrepareListener;
    private OnProgressListener onProgressListener;
    private OnErrorListener onErrorListener;

    public void setOnPrepareListener(OnPrepareListener onPrepareListener) {
        this.onPrepareListener = onPrepareListener;
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }



    public interface OnPrepareListener {
        /**
         * native层初始化完成通知java
         */
        void onPrepared();
    }

    public interface OnProgressListener {
        /**
         * native层不断回调进度通知java层
         * @param progress
         */
        void onProgress(int progress);
    }

    public interface OnErrorListener {
        /**
         * native层错误信息回调java层
         * @param error
         */
        void onError(int error);
    }

}
