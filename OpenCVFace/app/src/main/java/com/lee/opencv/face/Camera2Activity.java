package com.lee.opencv.face;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import com.lee.opencv.face.utils.CameraTool;
import com.lee.opencv.face.widget.AutoFitTextureView;

import java.io.File;

/**
 * @author jv.lee
 */
public class Camera2Activity extends AppCompatActivity {

    private CameraTool cameraTool;
    private OpenCvJni openCvJni;
    private boolean isTrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        AutoFitTextureView textureView = findViewById(R.id.texture);
        openCvJni = new OpenCvJni();
        cameraTool = new CameraTool(this, textureView);
        cameraTool.setPictureCallback(new CameraTool.PictureCallback() {
            @Override
            public void onPreviewFrame(byte[] data) {
//                if (!isTrue) {
//                    isTrue = true;
//                    openCvJni.postData(data,cameraTool.getWidth(),cameraTool.getHeight(),cameraTool.getCameraId());
//                }
                openCvJni.postData(data,cameraTool.getWidth(),cameraTool.getHeight(),cameraTool.getCameraId());
            }

            @Override
            public void onPicture(String path) {
                Toast.makeText(Camera2Activity.this, path, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextureChange(Surface surface, int width, int height) {
                openCvJni.setSurface(surface);
            }
        });
    }

    public void takePicture(View view) {
        cameraTool.takePicture();
    }

    public void switchPreview(View view) {
        cameraTool.switchCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraTool.startBackgroundThread();
        cameraTool.startPreview();

        String path = new File(Environment.getExternalStorageDirectory(), "lbpcascade_frontalface.xml").getAbsolutePath();
        openCvJni.init(path);
    }

    @Override
    protected void onPause() {
        cameraTool.stopPreview();
        cameraTool.stopBackgroundThread();
        super.onPause();
    }

}
