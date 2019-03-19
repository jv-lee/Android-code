package com.lee.code.plugin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lee.library.pluginlib.PluginManager;
import com.lee.library.pluginlib.ProxyActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PluginManager.getInstance().init(this);

        findViewById(R.id.btn_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加载apk文件
                String apkPath = AssetsUtils.copyAssetAndWrite(MainActivity.this,"pluginapp.apk");
                PluginManager.getInstance().loadApk(apkPath);
            }
        });

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到指定的Activity
                Intent intent = new Intent(MainActivity.this,ProxyActivity.class);
                intent.putExtra("className", "com.lee.code.pluginapp.PluginActivity");
                startActivity(intent);
            }
        });




    }
}
