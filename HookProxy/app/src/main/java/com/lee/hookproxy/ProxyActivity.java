package com.lee.hookproxy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author jv.lee
 */
public class ProxyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);
        Toast.makeText(this, "ProxyActivity ->", Toast.LENGTH_SHORT).show();
    }
}
