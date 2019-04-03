package com.jv.code.processkepplive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jv.code.processkepplive.activity.KeepManager;
import com.jv.code.processkepplive.service.ForegroundService;
import com.jv.code.processkepplive.service.LocalService;
import com.jv.code.processkepplive.service.RemoteService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //通过一像素activity 进行提权 adj等级变小   保活技术
        KeepManager.getInstance().registerKeep(this);

        //前台服务 保活技术
        startService(new Intent(this, ForegroundService.class));

        //启动双进程守护
        startService(new Intent(this, LocalService.class));
        startService(new Intent(this, RemoteService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeepManager.getInstance().unRegisterKeep(this);
    }
}
