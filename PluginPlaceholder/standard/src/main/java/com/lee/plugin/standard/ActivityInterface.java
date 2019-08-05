package com.lee.plugin.standard;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author jv.lee
 * @date 2019-08-05
 * @description 插件化标准接口 插件activity实现
 */
public interface ActivityInterface {

    void bindContext(Activity activity);

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

}
