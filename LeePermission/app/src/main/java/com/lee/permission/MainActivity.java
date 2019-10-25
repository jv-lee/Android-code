package com.lee.permission;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lee.permission.annotation.Permission;
import com.lee.permission.annotation.PermissionCancel;
import com.lee.permission.annotation.PermissionDenied;
import com.lee.permission.core.IPermission;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

        findViewById(R.id.btn_request2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionManager.getInstance()
                        .attach(MainActivity.this)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .listener(new IPermission() {
                            @Override
                            public void granted() {
                                Toast.makeText(MainActivity.this, "权限申请成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void cancel() {
                                Toast.makeText(MainActivity.this, "权限申请失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void denied() {
                                Toast.makeText(MainActivity.this, "权限申请失败，勾选不再提醒", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Permission(value = { Manifest.permission.READ_PHONE_STATE}, requestCode = 200)
    public void requestPermission() {
        Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();
    }

    @PermissionCancel
    public void permissionCancel() {
        Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied
    public void permissionDenied() {
        Toast.makeText(this, "权限申请失败，勾选不再提醒", Toast.LENGTH_SHORT).show();
    }
}
