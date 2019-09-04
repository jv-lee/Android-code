package com.lee.glide;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image);
        button = findViewById(R.id.btn);

        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Glide.with(MainActivity.this).load("https://img.iplaysoft.com/axdres/sidebar/lychee/imazing_v1.png").into(imageView);
            }
        });
    }
}
