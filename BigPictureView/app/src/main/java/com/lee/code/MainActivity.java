package com.lee.code;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lee.code.widget.BigPictureView;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BigPictureView ivImage = findViewById(R.id.iv_image);

        InputStream is = null;
        try {
            is = getAssets().open("image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImage.setImage(is);
    }
}
