package com.lee.opencv.face;

import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.lee.opencv.face.utils.CameraHelper;
import com.lee.opencv.face.utils.Utils;

import java.io.File;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private OpenCvJni openCvJni;
    private CameraHelper cameraHelper;
    int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    /**
     * 人脸识别
     */
    FaceDetector faceDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView surfaceView = findViewById(R.id.surface_view);
        openCvJni = new OpenCvJni();
        surfaceView.getHolder().addCallback(this);
        cameraHelper = new CameraHelper(cameraId);
        cameraHelper.setPreviewCallback(this);
        Utils.copyAssets(this, "lbpcascade_frontalface.xml");
    }

    @Override
    protected void onResume() {
        super.onResume();
        String path = new File(Environment.getExternalStorageDirectory(), "lbpcascade_frontalface.xml").getAbsolutePath();
        cameraHelper.startPreview();
        openCvJni.init(path);
    }

    public void switchPreview(View view) {
        cameraHelper.switchCamera();
        cameraId = cameraHelper.getCameraId();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        openCvJni.setSurface(holder.getSurface());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    boolean isTrue;

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
//        try {
//            SystemClock.sleep(100);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (!isTrue) {
//            isTrue = true;
//            openCvJni.postData(data, CameraHelper.WIDTH, CameraHelper.HEIGHT, cameraId);
//        }
        openCvJni.postData(data, CameraHelper.WIDTH, CameraHelper.HEIGHT, cameraId);
    }
}
