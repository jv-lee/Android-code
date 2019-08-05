package com.lee.plugin.placeholder;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 加载插件
     *
     * @param view
     */
    public void loadPlugin(View view) {
        PluginManager.getInstance(this).loadPlugin();
    }

    /**
     * 启动插件中的Activity
     *
     * @param view
     */
    public void startPluginActivity(View view) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plugin.apk");
        //获取插件包里的Activity
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
        ActivityInfo activityInfo = packageInfo.activities[0];

        Intent intent = new Intent(this, ProxyActivity.class);
        intent.putExtra("className", activityInfo.name);
        startActivity(intent);
    }
}
