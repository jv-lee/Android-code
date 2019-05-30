package com.lee.code.ffmpeg;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private MyPlayer myPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceView);

        //调用jni层设置监听
        myPlayer = new MyPlayer();
        myPlayer.setSurfaceView(surfaceView);
    }

    public void open(View view) {
        //在子线程调用native层函数 绘制视频图像， surfaceView会自动把每一帧数据回调在主线程绘制
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                File file = new File(Environment.getExternalStorageDirectory(), "input.mp4");
                myPlayer.start(file.getAbsolutePath());
            }
        });
    }

    public void codeAudio(View view) {
        File inFile = new File(Environment.getExternalStorageDirectory(), "input.mp3");
        File outFile = new File(Environment.getExternalStorageDirectory(), "input.pcm");
        myPlayer.sound(inFile.getAbsolutePath(),outFile.getAbsolutePath());
    }
}
