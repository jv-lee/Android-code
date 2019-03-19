package com.lee.code.imagecompress;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lee.code.imagecompress.utils.UriParseUtils;
import com.lee.library.imagecompress.CompressImageManager;
import com.lee.library.imagecompress.bean.Photo;
import com.lee.library.imagecompress.config.CompressConfig;
import com.lee.library.imagecompress.listener.CompressImage;
import com.lee.library.imagecompress.utils.CachePathUtils;
import com.lee.library.imagecompress.utils.CommonUtils;
import com.lee.library.imagecompress.utils.Constants;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements CompressImage.CompressListener {

    private CompressConfig compressConfig;//压缩配置
    private ProgressDialog dialog;
    private String cameraCachePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //运行时权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(perms[2]) == PackageManager.PERMISSION_DENIED){
                requestPermissions(perms, 200);
            }
        }

        compressConfig = CompressConfig.builder()
                .setUnCompressMinPixel(1000)//最小像素不压缩
                .setUnCompressNormalPixel(2000)//标准像素不压缩
                .setMaxPixel(1200)//长或宽不超过某像素
                .setMaxSize(200*1024)//压缩最大值
                .enablePixelCompress(true)//像素压缩
                .enableQualityCompress(true)//质量压缩
                .enableReserveRaw(true)//保留源文件
                .setCacheDir("")//没有设置的
                .setShowCompressDialog(true)//显示dialog
                .create();
    }


    public void show(View view) {
        compressMore();
    }

    private void compressMore() {
        // 测试多张图片同时压缩
        ArrayList<Photo> photos = new ArrayList<>();
        photos.add(new Photo("/storage/emulated/0/DCIM/Camera/IMG_20171108_151541.jpg"));
        photos.add(new Photo("/storage/emulated/0/DCIM/Camera/IMG_20171011_095724.jpg"));
        photos.add(new Photo("/storage/emulated/0/DCIM/Camera/IMG_20171011_092207.jpg"));
        photos.add(new Photo("/storage/emulated/0/DCIM/Camera/IMG_20170608_113509.jpg"));
        photos.add(new Photo("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1535449679877.jpg"));
        photos.add(new Photo("/storage/emulated/0/autoLite/cameraImg/1535016551150.jpg"));
        photos.add(new Photo("/storage/emulated/0/Download/微信图片_20171205095927.jpg"));
        photos.add(new Photo("/storage/emulated/0/Pictures/camera_20181115_111332.jpg"));
        photos.add(new Photo("/storage/emulated/0/Pictures/camera_20180706_173207.jpg"));
        compress(photos);
    }

    // 点击拍照
    public void camera(View view) {
        // FileProvider
        Uri outputUri;
        File file = CachePathUtils.getCameraCacheFile();
        ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            outputUri = UriParseUtils.getCameraOutPutUri(this, file);
        } else {
            outputUri = Uri.fromFile(file);
        }
        cameraCachePath = file.getAbsolutePath();
        // 启动拍照
        CommonUtils.hasCamera(this, CommonUtils.getCameraIntent(outputUri), Constants.CAMERA_CODE);
    }

    // 点击相册
    public void album(View view) {
        CommonUtils.openAlbum(this, Constants.ALBUM_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拍照返回
        if (requestCode == Constants.CAMERA_CODE && resultCode == RESULT_OK) {
            // 压缩（集合？单张）
            preCompress(cameraCachePath);
        }

        // 相册返回
        if (requestCode == Constants.ALBUM_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String path = UriParseUtils.getPath(this, uri);
                // 压缩（集合？单张）
                preCompress(path);
            }
        }
    }

    // 准备压缩，封装图片集合
    private void preCompress(String photoPath) {
        ArrayList<Photo> photos = new ArrayList<>();
        photos.add(new Photo(photoPath));
        if (!photos.isEmpty()) compress(photos);
    }

    // 开始压缩
    private void compress(ArrayList<Photo> photos) {
        if (compressConfig.isShowCompressDialog()) {
            Log.e("lee >>> ", "开启了加载框");
            dialog = CommonUtils.showProgressDialog(this, "压缩中……");
        }
        CompressImageManager.build(this, compressConfig, photos, this).compress();
    }

    @Override
    public void onCompressSuccess(ArrayList<Photo> arrayList) {
        Log.e("lee >>> ", "压缩成功");
        if (dialog != null && !isFinishing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onCompressFailed(ArrayList<Photo> arrayList, String error) {
        Log.e("lee >>> ", error);
        if (dialog != null && !isFinishing()) {
            dialog.dismiss();
        }
    }
}
