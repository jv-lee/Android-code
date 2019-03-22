package com.lee.code.childapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lee.library.hermeslib.ProcessManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProcessManager.getInstance().connect(this,"com.lee.code.hermes");

        findViewById(R.id.btn_getPerson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IUserManager userManager = ProcessManager.getInstance().getInstance(IUserManager.class);
                Toast.makeText(MainActivity.this, "----->"+userManager.getPerson(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
