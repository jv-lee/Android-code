package com.gionee.gnservice.module.integral;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.sdk.AmigoServiceSdk;
import com.gionee.gnservice.sdk.IAmigoServiceSdk;
import com.gionee.gnservice.sdk.INightModeHelper;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.MiscUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.SdkUtil;
import com.gionee.gnservice.utils.ToastUtil;

import amigoui.app.AmigoActionBar;
import amigoui.app.AmigoActivity;

/**
 * Created by jing on 17-8-24.
 */

public abstract class BaseAmigoActionBarActivity extends AmigoActivity implements
        IAmigoServiceSdk.OnLoginStatusChangeListener, INightModeHelper.OnNightModeChangeListener {
    private static final String TAG = "BaseAmigoActionBarActivity";
    private View mContentView;
    private IAppContext mAppContext;
    protected boolean mNeedShowNetworkError = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        initContentView();
        savedInstanceState(savedInstanceState);
        initAppContext();
        initVariables();
        initActionBar();
        preInitView();
        initView();
        postInitView();
        initData();
        initChameleonColor();
        if (SdkUtil.isCallBySdk(this)) {
            AmigoServiceSdk.getInstance().nightModeHelper().registerNightModeChangeListener(this);
        }
    }

    protected void initTheme() {
        setTheme(ResourceUtil.getStyleId(this, "AmigoServiceLightTheme"));
    }

    protected void initContentView() {
        int contentViewId = getLayoutResId();
        if (contentViewId > 0) {
            setContentView(contentViewId);
        } else {
            mContentView = contentView();
            if (mContentView != null) {
                setContentView(mContentView);
            }
        }
    }

    private View contentView() {
        return null;
    }

    protected int getLayoutResId() {
        return 0;
    }

    protected void savedInstanceState(Bundle savedInstanceState) {

    }

    protected void initAppContext() {
        if (SdkUtil.isCallBySdk(this)) {
            mAppContext = AmigoServiceSdk.getInstance().appContext();
        } else {
            mAppContext = (IAppContext) this.getApplication();
        }
    }

    protected IAppContext appContext() {
        return mAppContext;
    }

    protected void initVariables() {

    }

    protected void initActionBar() {
        AmigoActionBar actionBar = getAmigoActionBar();
        if (actionBar == null) {
            LogUtil.d(TAG, "getAmigoActionBar is null");
            return;
        }
        if (SdkUtil.isCallBySdk(this) && !AmigoServiceSdk.getInstance().isSupportPowerMode() && AmigoServiceSdk.getInstance().isNightMode()) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(ResourceUtil.getLayoutId(this, "activity_title"));
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(getActionbarTitle());

            if (!TextUtils.isEmpty(getActionbarRightTitle())) {
                actionBar.setDisplayShowCustomEnabled(true);
                View customView = LayoutInflater.from(this).inflate(ResourceUtil.getLayoutId(this, "uc_actionbar_customer"), null);
                TextView txtMenu = getView(customView, ResourceUtil.getWidgetId(this, "uc_txt_actionbar_customer_right"));
                txtMenu.setText(getActionbarRightTitle());
                txtMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onActionRightTextClick(v);
                    }
                });
                actionBar.setCustomView(customView, new AmigoActionBar.LayoutParams(AmigoActionBar.LayoutParams.WRAP_CONTENT,
                        AmigoActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.RIGHT));
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(View view, int id) {
        return (T) view.findViewById(id);
    }

    protected abstract String getActionbarTitle();

    protected String getActionbarRightTitle() {
        return null;
    }

    protected void onActionRightTextClick(View v) {

    }

    protected void preInitView() {

    }

    protected abstract void initView();

    protected void postInitView() {

    }

    protected void initData() {

    }

    private void initChameleonColor() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNeedShowNetworkError) {
            ToastUtil.showNetWorkErrorToast(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (SdkUtil.isCallBySdk(this)) {
            AmigoServiceSdk.getInstance().nightModeHelper().unRegisterNightModeChangeListener(this);
        }
    }

    @Override
    public void onNightModeChanged(boolean isNightMode) {
        recreate();
    }

    @Override
    public void onGetLoginStatus(boolean isLogin) {
        LogUtil.d(TAG, "onGetLoginStatus isLogin is:" + isLogin);
        if (isLogin) {
            return;
        }

        if (SdkUtil.isCallBySdk(this)) {
            finish();
            return;
        }

        if (MiscUtil.isAppOnForeground(this)) {
            try {
                Intent intent = new Intent(this, Class.forName("com.gionee.gnservice.UserCenterActivity"));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                LogUtil.e(TAG, "onGetLoginStatus e =" + e.toString());
            }
        }
        finish();
    }

}
