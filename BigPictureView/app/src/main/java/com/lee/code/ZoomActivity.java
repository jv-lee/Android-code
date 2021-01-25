package com.lee.code;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.lee.code.widget.cropimage.CropImageView;

/**
 * @author jv.lee
 * @date 2020/12/17
 * @description
 */
public class ZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        setImageParams();
    }


    private void setImageParams() {
        CropImageView image = findViewById(R.id.iv_image);
        image.setBounceEnable(true);
        image.enable();
        image.setRotateEnable(false);
        image.setShowImageRectLine(false);
        image.setCanShowTouchLine(false);
    }

}
