package com.lee.component.personal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lee.component.annotation.ARouter;

/**
 * @author jv.lee
 */
@ARouter(path = "/personal/Personal_Main2Activity")
public class Personal_Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_main2);
    }
}
