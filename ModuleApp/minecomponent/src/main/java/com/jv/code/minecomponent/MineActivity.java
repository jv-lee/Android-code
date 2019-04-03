package com.jv.code.minecomponent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MineActivity extends AppCompatActivity {
    public static String EXT_TAG = "MineActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
    }
}
