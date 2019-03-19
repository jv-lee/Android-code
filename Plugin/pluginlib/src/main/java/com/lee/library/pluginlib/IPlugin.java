package com.lee.library.pluginlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public interface IPlugin {
    int FROM_INTERNAL = 0;//内部
    int FROM_EXTERNAL = 1;//外部

    void attach(Activity activity);

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
