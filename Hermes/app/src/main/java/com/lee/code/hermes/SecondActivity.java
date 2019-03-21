package com.lee.code.hermes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lee.code.hermes.process.IUserManager;
import com.lee.code.hermes.process.UserManager;
import com.lee.library.hermeslib.ProcessManager;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ProcessManager.getInstance().connect(this);

        //获取对象  执行方法
        UserManager.getsInstance().getPerson();
    }

    public void clickUserManager(View view) {
        ProcessManager.getInstance().getInstance(IUserManager.class);
    }

    public void clickGetUser(View view) {

    }
}
