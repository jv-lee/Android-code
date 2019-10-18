package com.jv.code.processkepplive.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.jv.code.processkepplive.KeepTest;

public class KeepActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        //设置activity在界面左上角
        window.setGravity(Gravity.START | Gravity.TOP);

        WindowManager.LayoutParams attr = window.getAttributes();
        attr.width = 1;
        attr.height = 1;
        attr.x = 0;
        attr.y = 0;

        window.setAttributes(attr);
        KeepManager.getInstance().setKeep(this);

        KeepTest.keepTest();
    }
}
