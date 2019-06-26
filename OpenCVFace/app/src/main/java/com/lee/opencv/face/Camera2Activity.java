package com.lee.opencv.face;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;

import com.lee.opencv.face.utils.CameraTool;
import com.lee.opencv.face.widget.AutoFitTextureView;

/**
 * @author jv.lee
 */
public class Camera2Activity extends AppCompatActivity {

    private CameraTool cameraTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        AutoFitTextureView textureView = (AutoFitTextureView) findViewById(R.id.texture);
        cameraTool = new CameraTool(this, textureView);
    }

    public void takePicture(View view) {
    }

    public void switchPreview(View view) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraTool.startBackgroundTherad();
        cameraTool.startPreview();
    }

    @Override
    protected void onPause() {
        cameraTool.stopPreview();
        cameraTool.stopBackgroundThread();
        super.onPause();
    }

}
