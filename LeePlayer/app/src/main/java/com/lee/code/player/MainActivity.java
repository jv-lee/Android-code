package com.lee.code.player;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private LeePlayer mLeePlayer;
    private SurfaceView surfaceView;
    private SeekBar seekBar;
    private Button btnPlay, btnPause;

    private final String[] permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        initView();
        checkPermission();
        initNative();
    }

    private void initView() {
        surfaceView = findViewById(R.id.surface);
        seekBar = findViewById(R.id.seek);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        seekBar.setOnSeekBarChangeListener(this);
    }

    private void checkPermission() {
        boolean isGranted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //无权限
                isGranted = false;
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            if (!isGranted) {
                requestPermissions(permissions,REQUEST_CODE);
            }
        }
    }

    private void initNative() {
        mLeePlayer = new LeePlayer();
        mLeePlayer.setSurfaceView(surfaceView);
        mLeePlayer.setDataSource(new File(Environment.getExternalStorageDirectory(), "input2.mp4").getAbsolutePath());
        mLeePlayer.setOnPrepareListener(new LeePlayer.OnPrepareListener() {
            @Override
            public void onPrepared() {
                mLeePlayer.start();
            }
        });
    }

    public void play(View view) {
        mLeePlayer.prepare();
    }

    public void pause(View view) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
