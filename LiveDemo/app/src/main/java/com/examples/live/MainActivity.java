package com.examples.live;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

import com.examples.live.meida.Camera2Helper;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private LivePusher mLivePusher;
    Camera2Helper mCamera2Helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView surfaceView = findViewById(R.id.surface_view);
        mCamera2Helper = new Camera2Helper(this,0,800,480);
        mCamera2Helper.setPreviewDisplay(surfaceView.getHolder());

//        mLivePusher = new LivePusher(this, 800, 480, 800_800, 10, Camera.CameraInfo.CAMERA_FACING_BACK);
//        mLivePusher.setPreviewDisplay(surfaceView.getHolder());
    }

    public void switchPreview(View view) {
        mCamera2Helper.switchCamera();
//        mLivePusher.switchCamera();
    }

    public void startPreview(View view) {
//        mLivePusher.startLive("rtmp://192.168.31.140/myapp");
        mCamera2Helper.startPreview();
    }

    public void pausePreview(View view) {
        mCamera2Helper.stopPreview();
    }
}
