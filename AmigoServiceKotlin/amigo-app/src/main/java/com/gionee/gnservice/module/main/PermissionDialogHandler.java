package com.gionee.gnservice.module.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.gionee.gnservice.R;
import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.SharedPrefHelper;
import com.gionee.gnservice.entity.ServiceInfo;
import com.gionee.gnservice.utils.PhoneUtil;

import java.lang.ref.WeakReference;

/**
 * Created by caocong on 4/20/17.
 */

public class PermissionDialogHandler {
    private static final String TAG = PermissionDialogHandler.class.getSimpleName();
    private WeakReference<Activity> mActivity;
    private AlertDialog mTrafficAlertDlg;
    private IAppContext mAppContext;


    public PermissionDialogHandler(@NonNull Activity activity, @NonNull IAppContext appContext) {
        mActivity = new WeakReference<Activity>(activity);
        mAppContext = appContext;
    }

    public boolean showPermissionDialog(ServiceInfo moduleInfo) {
        if (needShowPermissionDialog(moduleInfo) &&
                "com.gionee.gnservice.module.bbs.AmigoBBSActivity".equals(moduleInfo.getTarget())) {
            showBbsPermissionDialog(moduleInfo);
            return true;
        }
        return false;
    }

    private void showBbsPermissionDialog(final ServiceInfo moduleInfo) {
        Activity activity = mActivity.get();
        if (activity == null) {
            return;
        }
        final BaseModule module = new BaseModule(activity, moduleInfo);
        if (PhoneUtil.isSupportSystemPermissionAlert()) {
            module.startModule();
            return;
        }

        if (mTrafficAlertDlg != null && mTrafficAlertDlg.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.uc_permission_request_dialog_title);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.uc_dialog_setting_permission_confirm, null);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_message);
        tvMessage.setText(R.string.uc_permission_request_dialog_bbs_content);
        final CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.uc_permission_request_dialog_positive,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cb.isChecked()) {
                            savePermissionSetting(moduleInfo.getTarget(), true);
                        }
                        module.startModule();
                        mTrafficAlertDlg = null;
                    }
                });
        builder.setNegativeButton(R.string.uc_permission_request_dialog_negative,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTrafficAlertDlg = null;
                    }
                });
        mTrafficAlertDlg = builder.create();
        mTrafficAlertDlg.show();
    }

    private void savePermissionSetting(String key, boolean value) {
        SharedPrefHelper sp = mAppContext.sharedPrefHelper();
        sp.putBoolean(key, value);
    }

    private boolean needShowPermissionDialog(ServiceInfo moduleInfo) {
        SharedPrefHelper sp = mAppContext.sharedPrefHelper();
        return moduleInfo.isNeedShowTrafficAlertFirst() && !sp.getBoolean(moduleInfo.getTarget(),
                false);
    }

}
