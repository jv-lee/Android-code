package com.gionee.gnservice.base;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gionee.gnservice.sdk.AmigoServiceSdk;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.SdkUtil;

public abstract class BaseActivity extends Activity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private IAppContext mAppContext;
    protected View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (!SdkUtil.isCallBySdk(this) || AmigoServiceSdk.getInstance().isSupportPowerMode()) {
            ChameleonColorManager.getInstance().onDestroy(this);
        } else {
            ChameleonColorManager.getInstance().unregisterNoChangeColor(this);
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    private void initAppContext() {
        if (SdkUtil.isCallBySdk(this)) {
            mAppContext = AmigoServiceSdk.getInstance().appContext();
        } else {
            mAppContext = (IAppContext) this.getApplication();
        }
    }

    protected void initActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            LogUtil.e(TAG, "getActionBar return null, somewhere is error?");
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
                View customView = LayoutInflater.from(this).inflate(ResourceUtil.getLayoutId(this, "uc_actionbar_customer_android_activity"), null);
                TextView txtMenu = getView(customView, ResourceUtil.getWidgetId(this, "uc_txt_actionbar_customer_right"));
                txtMenu.setText(getActionbarRightTitle());
                txtMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onActionRightTextClick(v);
                    }
                });
                actionBar.setCustomView(customView, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL | Gravity.RIGHT));
            }
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

    @SuppressWarnings("unused")
    protected void savedInstanceState(Bundle savedInstanceState) {}

    protected void preInitView() {}

    protected void postInitView() {}

    protected void addChameleonColorView() {}

    protected IAppContext appContext() {
        return mAppContext;
    }

    protected void initVariables() {}

    protected abstract void initView();

    protected int getLayoutResId() {
        return -1;
    }

    protected View contentView() {
        return null;
    }

    protected abstract String getActionbarTitle();

    protected String getActionbarRightTitle() {
        return null;
    }

    protected void initData() {}

    protected void onActionRightTextClick(View view) {}

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
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            super.startActivityForResult(intent, requestCode, options);
        } else {
            LogUtil.e(TAG, "Activity is not exist");
        }
    }

}
