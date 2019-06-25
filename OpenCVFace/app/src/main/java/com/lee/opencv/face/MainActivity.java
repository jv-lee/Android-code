package com.lee.opencv.face;

import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.media.FaceDetector;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.lee.opencv.face.utils.Camera2Helper;
import com.lee.opencv.face.utils.Utils;

import java.io.File;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private OpenCvJni openCvJni;
    private Camera2Helper cameraHelper2;
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private boolean isTrue;

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
        Utils.copyAssets(this, "lbpcascade_frontalface.xml");

        cameraHelper2 = new Camera2Helper(this, 1, WIDTH, HEIGHT);
        cameraHelper2.setPreviewDisplay(surfaceView.getHolder());
        cameraHelper2.setOnSurfaceChange(new Camera2Helper.OnSurfaceChange() {
            @Override
            public void onPreviewFrame(byte[] data) {
//                try {
//                    SystemClock.sleep(100);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (!isTrue) {
//                    isTrue = true;
//                    openCvJni.postData(data, cameraHelper2.getHeight(), cameraHelper2.getWidth(), cameraHelper2.getCameraId());
//                }
                openCvJni.postData(data, cameraHelper2.getHeight(), cameraHelper2.getWidth(), CameraCharacteristics.LENS_FACING_FRONT);
            }

            @Override
            public void onChanged(SurfaceHolder holder, int w, int h) {
                openCvJni.setSurface(holder.getSurface());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String path = new File(Environment.getExternalStorageDirectory(), "lbpcascade_frontalface.xml").getAbsolutePath();
        cameraHelper2.startPreview();
        openCvJni.init(path);
    }

    public void switchPreview(View view) {
        cameraHelper2.switchCamera();
    }

}
