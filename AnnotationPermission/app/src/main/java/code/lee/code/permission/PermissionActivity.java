package code.lee.code.permission;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import code.lee.code.annotation.OnNeverAskAgain;
import code.lee.code.annotation.OnPermissionDenied;
import code.lee.code.annotation.OnShowRationale;
import code.lee.code.annotation.OnPermission;
import code.lee.code.library.PermissionManager;
import code.lee.code.library.listener.PermissionRequest;

/**
 * @author jv.lee
 */
public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
    }

    public void camera(View view) {
        PermissionManager.request(this, new String[]{Manifest.permission.CAMERA});
    }

    // 在需要获取权限的地方注释
    @OnPermission()
    void showCamera() {
        Log.e("neteast >>> ", "showCamera()");
    }

    // 提示用户为何要开启权限
    @OnShowRationale()
    void showRationaleForCamera(final PermissionRequest request) {
        Log.e("neteast >>> ", "showRationaleForCamera()");
        new AlertDialog.Builder(this)
                .setMessage("提示用户为何要开启权限")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        // 再次执行权限请求
                        request.proceed();
                    }
                })
                .show();
    }

    // 用户选择拒绝时的提示
    @OnPermissionDenied()
    void showDeniedForCamera() {
        Log.e("neteast >>> ", "showDeniedForCamera()");
    }

    // 用户选择不再询问后的提示
    @OnNeverAskAgain()
    void showNeverAskForCamera() {
        Log.e("neteast >>> ", "showNeverAskForCamera()");
        new AlertDialog.Builder(this)
                .setMessage("用户选择不再询问后的提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Log.e("neteast >>> ", "showNeverAskForCamera() >>> Dialog");
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("neteast >>> ", "onRequestPermissionsResult()");
        PermissionManager.onRequestPermissionsResult(this, requestCode, grantResults);
    }


}
