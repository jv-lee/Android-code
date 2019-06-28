package com.lee.open.photo.face;

import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.lee.open.photo.utils.CameraHelper;


/**
 * @author jv.lee
 * @date 2019/6/27.
 * @description
 */
public class FaceJni {
    private static final int CHAECK_FACE = 1;
    private long self;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private CameraHelper mCameraHelper;
    private Face mFace;

    static {
        System.loadLibrary("native-lib");
    }

    public native long nativeInit(String path, String seeta);

    public native void nativeStart(long self);

    private native Face nativeDetector(long self, byte[] data, int cameraId, int width, int height);

    public FaceJni(String path, String seeta, CameraHelper cameraHelper) {
        self = nativeInit(path, seeta);
        mBackgroundThread = new HandlerThread("faceThread");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                mFace = nativeDetector(self, (byte[]) msg.obj, Camera.CameraInfo.CAMERA_FACING_FRONT, CameraHelper.WIDTH, CameraHelper.HEIGHT);
            }
        };
    }


    public void startTrack() {
        nativeStart(self);
    }

    /**
     * 检测人脸 耗时子线程操作
     *
     * @param data
     */
    public void detector(byte[] data) {
        //先把之前的消息移除
        mBackgroundHandler.removeMessages(CHAECK_FACE);
        //加入新的消息任务
        Message message = mBackgroundHandler.obtainMessage(CHAECK_FACE);
        message.obj = data;
        mBackgroundHandler.sendMessage(message);
    }

    public Face getFace() {
        return mFace;
    }
}
