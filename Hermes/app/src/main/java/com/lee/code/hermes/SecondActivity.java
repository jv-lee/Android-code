package com.lee.code.hermes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lee.code.hermes.process.IUserManager;
import com.lee.code.hermes.process.Person;
import com.lee.code.hermes.process.UserManager;
import com.lee.library.hermeslib.ProcessManager;

public class SecondActivity extends AppCompatActivity {
    IUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ProcessManager.getInstance().connect(this);

        findViewById(R.id.btn_getUserManager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager = ProcessManager.getInstance().getInstance(IUserManager.class);
            }
        });

        findViewById(R.id.btn_clickGetUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SecondActivity.this, "-------->" + userManager.getPerson(), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_setPerson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager.setPerson(new Person("我是子进程数据","88888888"));
            }
        });

    }


}
