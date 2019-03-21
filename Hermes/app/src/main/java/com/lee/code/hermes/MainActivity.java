package com.lee.code.hermes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lee.code.hermes.process.UserManager;
import com.lee.library.hermeslib.ProcessManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProcessManager.getInstance().register(UserManager.class);

    }
}
