package com.lee.code.livepush;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

/**
 * @author  jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private LivePusher livePusher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SurfaceView surfaceView = findViewById(R.id.surface_view);
        livePusher = new LivePusher(this, 800, 480, 800_000, 10, Camera.CameraInfo.CAMERA_FACING_BACK);
        //  设置摄像头预览的界面
        livePusher.setPreviewDisplay(surfaceView.getHolder());
        TextView textView = findViewById(R.id.tv_content);
        textView.setText(helloJni());
    }

    public native String helloJni();

    public void switchCamera(View view) {
        livePusher.switchCamera();
    }

    public void startLive(View view) {

    }

    public void stopLive(View view) {
    }

}
