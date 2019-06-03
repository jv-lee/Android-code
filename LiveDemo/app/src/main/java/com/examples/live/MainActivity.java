package com.examples.live;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private LivePusher mLivePusher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView surfaceView = findViewById(R.id.surface_view);
        mLivePusher = new LivePusher(this, 800, 480, 800_800, 10, Camera.CameraInfo.CAMERA_FACING_BACK);
        mLivePusher.setPreviewDisplay(surfaceView.getHolder());
    }

    public void switchPreview(View view) {
        mLivePusher.switchCamera();
    }

    public void startPreview(View view) {
        mLivePusher.startLive("rtmp://192.168.31.140/myapp");
    }

    public void pausePreview(View view) {
    }
}
