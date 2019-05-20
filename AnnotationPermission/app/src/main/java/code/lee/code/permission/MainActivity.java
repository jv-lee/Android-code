package code.lee.code.permission;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import code.lee.code.permission.intent.IntentRequest;
import code.lee.code.permission.intent.IntentManager;
import code.lee.code.permission.permission.PermissionRequest;
import code.lee.code.permission.permission.PermissionManager;


/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionManager.getInstance()
                .attach(this)
                .request(Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .listener(new PermissionRequest() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFiled(String permission) {
                        //返回请求失败 打开设置界面 让用户手动设置权限请求
                        Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });

        IntentManager.getInstance().startActForResult(this, IntentActivity.class, new IntentRequest() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == IntentManager.RESULT_CODE) {
                    Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
