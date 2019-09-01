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

    private String[] datas = {
            "http://img.netbian.com/file/2019/0320/842f282d57d8fddf08ae3e6c66d92d99.jpg",
            "http://img.netbian.com/file/2019/0320/0da6149458eb0abcd8a6068602c8cfde.jpg",
            "http://img.netbian.com/file/2018/0504/18cb732d8e8a0ee821dfa63ed1759354.jpg",
            "http://img.netbian.com/file/2019/0318/3070542f409f27757af4df3addc6dcad.jpg",
            "http://img.netbian.com/file/2019/0111/9193761b0168de21254c4291c0b7c945.jpg",
            "http://img.netbian.com/file/2019/0215/1782bef2b6cea834a38b282ed4acb13f.jpg",
            "http://img.netbian.com/file/2019/0318/242059034ad863651a7dc92202f57d56.jpg",
            "http://img.netbian.com/file/2019/0307/212f90387cad6158c00863671391f009.jpg",
            "http://img.netbian.com/file/2019/0307/212f90387cad6158c00863671391f009.jpg",
            "http://img.netbian.com/file/2019/0317/ab2913aa61cccde27958f9ef5cab10b9.jpg",
            "http://img.netbian.com/file/2019/0317/748f1d72afba8a9764d98bb7fda2b242.jpg",
            "http://img.netbian.com/file/2019/0312/e70d8c660d7321b25096cb7247f9a11a.jpg",
            "http://img.netbian.com/file/2019/0312/1b6eff5cae7432ce61452a828a76153a.jpg",
            "http://img.netbian.com/file/2019/0312/84b4b5a8ba3e60871464cfdaa261da55.jpg",
            "http://img.netbian.com/file/2019/0306/b9603babcb44ef9d4dde3a774c50f54d.jpg",
            "http://img.netbian.com/file/2019/0306/1021b1bf254ff885edfefa6941cf01fa.jpg",
            "http://img.netbian.com/file/2019/0306/5674ff28b2e0e70441318fdaf2182604.jpg",
            "https://hellorfimg.zcool.cn/preview260/671010355.jpg",
            "https://hellorfimg.zcool.cn/preview260/1017625264.jpg",
            "https://hellorfimg.zcool.cn/preview260/641967358.jpg",
            "https://hellorfimg.zcool.cn/preview260/684042022.jpg",
            "https://hellorfimg.zcool.cn/preview260/680447980.jpg",
            "https://hellorfimg.zcool.cn/preview260/663449998.jpg",
            "https://hellorfimg.zcool.cn/preview260/569348728.jpg",
            "https://hellorfimg.zcool.cn/preview260/582849952.jpg",
            "https://hellorfimg.zcool.cn/preview260/1039890337.jpg",
            "https://hellorfimg.zcool.cn/preview260/1012020988.jpg",
            "https://hellorfimg.zcool.cn/preview260/364802354.jpg",
            "https://hellorfimg.zcool.cn/preview260/1071287231.jpg",





    };

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
        Glide.with(this).load("http://img.netbian.com/file/2019/0320/842f282d57d8fddf08ae3e6c66d92d99.jpg").loading(android.R.drawable.ic_menu_send).listener(new RequestListener() {
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
        for (int i = 0; i < datas.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            scrool_line.addView(imageView);
            Glide.with(this).load(datas[i]).loading(android.R.drawable.ic_menu_send).listener(new RequestListener() {
                @Override
                public boolean onException() {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource) {
                    return false;
                }
            }).into(imageView);
        }
    }
}
