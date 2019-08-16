package com.gionee.gnservice.sdk.member;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.base.webview.BaseWebViewActivity;
import com.gionee.gnservice.sdk.AmigoServiceSdk;
import com.gionee.gnservice.sdk.IAmigoServiceSdk;
import com.gionee.gnservice.sdk.INightModeHelper;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.MiscUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.SdkUtil;
import com.gionee.gnservice.utils.ToastUtil;

/**
 * Created by caocong on 5/16/17.
 */
public abstract class BaseActionbarTransparentActivity extends Activity implements IAmigoServiceSdk.OnLoginStatusChangeListener, INightModeHelper.OnNightModeChangeListener {
    private static final String TAG = BaseActionbarTransparentActivity.class.getSimpleName();
    private IAppContext mAppContext;
    protected View mContentView;

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.showNetWorkErrorToast(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

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
        appContext().accountHelper().registerLoginStatusChangeListener(this);
        if (SdkUtil.isCallBySdk(this)) {
            AmigoServiceSdk.getInstance().nightModeHelper().registerNightModeChangeListener(this);
        }
    }

    private void initChameleonColor() {
        /*if (!SdkUtil.isCallBySdk(this) || AmigoServiceSdk.getInstance().isSupportPowerMode()) {
            LogUtil.d(TAG, "need to register power save mode");
            ChameleonColorManager.getInstance().onCreate(this);
            if (ChameleonColorManager.isPowerSavingMode()) {
                addChameleonColorView();
            }
        } else {
            LogUtil.d(TAG, "no need change color");
            ChameleonColorManager.getInstance().registerNoChangeColor(this);
        }*/
    }

    protected void addChameleonColorView() {

    }

    private void setActionbarTransparent() {
        View decorView = getWindow().getDecorView();
        if (decorView != null) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    protected void initTheme() {
        setTheme(ResourceUtil.getStyleId(this, "AmigoServiceTranslucentActionBar"));
    }

    protected void initContentView() {
        setActionbarTransparent();
        int contentViewId = getLayoutResId();
        if (contentViewId != -1) {
            setContentView(contentViewId);
        } else {
            mContentView = contentView();
            if (mContentView != null) {
                setContentView(mContentView);
            }
        }
    }

    protected void preInitView() {

    }

    protected void postInitView() {

    }

    @SuppressWarnings("unused")
    protected void savedInstanceState(Bundle savedInstanceState) {
    }

    private void initAppContext() {
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

    @Override
    public void onNightModeChanged(boolean isNightMode) {
        recreate();
    }

    protected abstract void initView();

    protected int getLayoutResId() {
        return -1;
    }

    protected View contentView() {
        return null;
    }

    protected abstract String getActionbarTitle();

    protected void initData() {

    }

    protected void initActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            LogUtil.d(TAG, "getAmigoActionBar is null");
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(getActionbarTitle());
    }

    @Override
    public void onGetLoginStatus(boolean isLogin) {
        LogUtil.d(TAG, "onGetLoginStatus isLogin=" + isLogin);
        if (isLogin) {
            return;
        }

        BaseWebViewActivity.clearCookie(this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //对用户按home icon的处理，本例只需关闭activity，就可返回上一activity，即主activity。
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(View view, int id) {
        return (T) view.findViewById(id);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (intent.resolveActivity(getPackageManager()) != null) {
            super.startActivityForResult(intent, requestCode, options);
        } else {
            LogUtil.e(TAG, "Activity is not exist");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (!SdkUtil.isCallBySdk(this) || AmigoServiceSdk.getInstance().isSupportPowerMode()) {
            ChameleonColorManager.getInstance().onDestroy(this);
        } else {
            ChameleonColorManager.getInstance().unregisterNoChangeColor(this);
        }*/
        appContext().accountHelper().unRegisterLoginStatusChangeListener(this);
        if (SdkUtil.isCallBySdk(this)) {
            AmigoServiceSdk.getInstance().nightModeHelper().unRegisterNightModeChangeListener(this);
        }
    }

}
