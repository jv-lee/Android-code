package com.lee.code.glide;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lee.code.glide.core.Glide;
import com.lee.code.glide.listener.RequestListener;

public class MainActivity extends AppCompatActivity {
    LinearLayout scrool_line;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrool_line = findViewById(R.id.scrool_line);
        //最简单的请求图片方式
        verifyStoragePermissions(this);
    }

    private void verifyStoragePermissions(MainActivity mainActivity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(mainActivity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                //没有权限，去申请获取权限，会弹出对话框
                ActivityCompat.requestPermissions(mainActivity,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void single(View view) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrool_line.addView(imageView);
        Glide.with(this).load("https://img3.mukewang.com/szimg/5ac2dfe100014a9005400300.jpg").loading(android.R.drawable.ic_menu_send).listener(new RequestListener() {
            @Override
            public boolean onException() {
                Toast.makeText(MainActivity.this, "onNo", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource) {
                Toast.makeText(MainActivity.this, "onRes", Toast.LENGTH_SHORT).show();
                return false;
            }
        }).into(imageView);
    }

    public void more(View view) {
    }
}
