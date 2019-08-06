package com.lee.plugin.app;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * @author jv.lee
 */
public class PluginActivity extends BaseActivity {

    private final String ACTION = "com.lee.plugin.placeholder.ACTION";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);

        Toast.makeText(activity, "我是插件Activity - PluginActivity", Toast.LENGTH_SHORT).show();

        findViewById(R.id.btn_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, TestActivity.class));
            }
        });
        findViewById(R.id.btn_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(activity, TestService.class));
            }
        });
        findViewById(R.id.btn_register_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ACTION);
                registerReceiver(new TestReceiver(), intentFilter);
            }
        });
        findViewById(R.id.btn_send_receiver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(ACTION);
                sendBroadcast(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
