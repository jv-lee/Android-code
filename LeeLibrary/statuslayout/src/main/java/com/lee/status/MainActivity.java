package com.lee.status;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusLayout statusLayout = findViewById(R.id.status_layout);
        statusLayout.setLoadingProgressColor(Color.BLUE);
        statusLayout.setOnRestartListener(new StatusLayout.OnRestartListener() {
            @Override
            public void onRestart() {
                Toast.makeText(MainActivity.this, "重新加载", Toast.LENGTH_SHORT).show();
            }
        });
        statusLayout.setStatus(StatusLayout.STATUS_LOADING);
    }
}
