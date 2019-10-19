package com.lee.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.lee.library.core.IPermission;
import com.lee.library.utils.PermissionUtils;

/**
 * @author jv.lee
 * @date 2019-10-19
 * @description 权限处理Activity
 */
public class PermissionActivity extends Activity {

    /**
     * 接收用户传递的权限组，权限处理标示
     */
    private final static String PARAM_PERMISSION = "param_permission";
    private final static String PARAM_REQUEST_CODE = "param_request_code";
    public final static int PARAM_REQUEST_CODE_DEFAULT = -1;

    private String[] permissions;
    private int requestCode;
    private static IPermission permissionListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        permissions = getIntent().getStringArrayExtra(PARAM_PERMISSION);
        requestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE, PARAM_REQUEST_CODE_DEFAULT);

        if (permissions == null && requestCode < 0 && permissionListener == null) {
            this.finish();
            return;
        }

        //授权检测处理
        boolean permissionRequest = PermissionUtils.hasPermissionRequest(this, permissions);
        if (permissionRequest) {
            //通过舰艇接口告诉外界，已经授权了
            permissionListener.ganted();
            this.finish();
            return;
        }

        //未授权，申请权限
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    /**
     * 处理权限申请结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //返回结果，验证是否完全成功
        if (PermissionUtils.requestPermissionSuccess(grantResults)) {
            //通过接口回调外界 权限申请通过
            permissionListener.ganted();
            this.finish();
            return;
        }

        //如果用户点击了，拒绝 （不再提示打勾操作） 等操作，告诉外界
        if (!PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)) {
            //用户拒绝不再提醒 ，通过接口告诉外界被拒绝（不再提示打勾）
            permissionListener.denied();
            this.finish();
            return;
        }

        //权限被取消了
        permissionListener.cancel();
        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
        //不需要有动画效果
        overridePendingTransition(0, 0);
    }

    /**
     * 把当前权限申请Activity暴露出去
     */
    public static void requestPermissionAction(Context context, String[] permissions, int requestCode, IPermission iPermission) {
        permissionListener = iPermission;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_REQUEST_CODE,requestCode);
        bundle.putStringArray(PARAM_PERMISSION,permissions);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }
}
