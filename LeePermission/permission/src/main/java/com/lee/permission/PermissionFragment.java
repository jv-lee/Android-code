package com.lee.permission;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.lee.permission.core.IPermission;
import com.lee.permission.utils.PermissionUtils;

/**
 * @author jv.lee
 * @date 2019/10/25.
 * @description
 */
public class PermissionFragment extends Fragment {

    /**
     * 接收用户传递的权限组，权限处理标示
     */
    private final static String PARAM_PERMISSION = "param_permission";
    private final static String PARAM_REQUEST_CODE = "param_request_code";
    public final static int PARAM_REQUEST_CODE_DEFAULT = -1;

    private IPermission permissionListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        String[] permissions = getArguments().getStringArray(PARAM_PERMISSION);
        int requestCode = getArguments().getInt(PARAM_REQUEST_CODE, PARAM_REQUEST_CODE_DEFAULT);

        if (permissions == null && requestCode < 0 && permissionListener == null) {
            return;
        }

        //授权检测处理
        boolean permissionRequest = PermissionUtils.hasPermissionRequest(getActivity(), permissions);
        if (permissionRequest) {
            //通过舰艇接口告诉外界，已经授权了
            permissionListener.granted();
            return;
        }

        //未授权，申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }else{
            Toast.makeText(context, "不支持5.0以下设备", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //返回结果，验证是否完全成功
        if (PermissionUtils.requestPermissionSuccess(grantResults)) {
            //通过接口回调外界 权限申请通过
            permissionListener.granted();
            return;
        }

        //如果用户点击了，拒绝 （不再提示打勾操作） 等操作，告诉外界
        if (!PermissionUtils.shouldShowRequestPermissionRationale(getActivity(), permissions)) {
            //用户拒绝不再提醒 ，通过接口告诉外界被拒绝（不再提示打勾）
            permissionListener.denied();
            //不仅仅要提醒用户，还需要自动跳转到手机设置界面
            if (PermissionManager.getInstance().isStartSettings()) {
                PermissionUtils.startAndroidSettings(getActivity());
            }
            return;
        }

        //权限被取消了
        permissionListener.cancel();
    }

    /**
     * 把当前权限申请Activity暴露出去
     */
    public static PermissionFragment getPermissionFragment(String[] permissions, int requestCode, IPermission iPermission) {
        PermissionFragment fragment = new PermissionFragment();
        fragment.setPermissionListener(iPermission);

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_REQUEST_CODE, requestCode);
        bundle.putStringArray(PARAM_PERMISSION, permissions);

        fragment.setArguments(bundle);
        return fragment;
    }

    private void setPermissionListener(IPermission iPermission) {
        this.permissionListener = iPermission;
    }

}
