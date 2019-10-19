package com.lee.permissions;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lee.library.annotation.Permission;
import com.lee.library.annotation.PermissionCancel;
import com.lee.library.annotation.PermissionDenied;

/**
 * @author jv.lee
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRequest();
            }
        });
    }



    @Permission(value = Manifest.permission.READ_EXTERNAL_STORAGE,requestCode = 200)
    public void testRequest(){
        Toast.makeText(this, "权限申请成功...", Toast.LENGTH_SHORT).show();
    }

    @PermissionCancel
    public void testCancel(){
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
    }

    @PermissionDenied
    public void testDenied(){
        Toast.makeText(this, "权限被拒绝（用户勾选了，不再提醒）", Toast.LENGTH_SHORT).show();
    }

}
