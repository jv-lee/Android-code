package com.lee.opencv.face;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;

import com.lee.opencv.face.utils.CameraTool;

public class Camera2Activity extends AppCompatActivity {

    private CameraTool camera2Tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        camera2Tool = new CameraTool(this, (TextureView) findViewById(R.id.texture));
    }

    public void takePicture(View view) {
        camera2Tool.takePic();
    }

    public void switchPreview(View view) {
        camera2Tool.exchangeCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera2Tool.releaseCamera();
        camera2Tool.releaseThread();
    }
}
