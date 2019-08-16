package com.gionee.gnservice.module.setting;

import amigoui.app.AmigoActionBar;
import amigoui.preference.AmigoPreference;
import amigoui.preference.AmigoPreferenceActivity;
import amigoui.preference.AmigoSwitchPreference;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.gionee.account.sdk.itf.GioneeAccount;
import com.gionee.feedback.FeedbackApi;
import com.gionee.feedback.exception.FeedBackException;
import com.gionee.gnservice.R;
import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.SharedPrefHelper;
import com.gionee.gnservice.sdk.AmigoServiceSdk;
import com.gionee.gnservice.sdk.IAmigoServiceSdk;
import com.gionee.gnservice.statistics.StatisticsEvents;
import com.gionee.gnservice.statistics.StatisticsUtil;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PhoneUtil;
import com.gionee.gnservice.utils.RepeatClickUtil;
import com.gionee.gnservice.utils.SdkUtil;


/**
 * Created by caocong on 1/9/17.
 */
public class MemberSettingActivity extends AmigoPreferenceActivity implements View.OnClickListener,
        IMemberSettingContract.View, IAmigoServiceSdk.OnLoginStatusChangeListener {
    private static final String TAG = "MemberSettingActivity";
    public static final String SHARE_KEY_NOTIFY_ENABLE = "key_notify_switch_enable";
    private static final String PREF_KEY_PUSH = "key_member_setting_push_switch";
    private static final String PREF_KEY_FEEDBACK = "key_member_feedback_setting";
    private IAppContext mAppContext;
    private AmigoPreference mFeedbackView;
    private AmigoSwitchPreference mPushView;
    private Button mBtnChangeAccount;
    private IMemberSettingContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAppContext();
        initVariables();
        initActionbar();
        addPreferencesFromResource(R.xml.uc_preference_member_setting);
        setContentView(R.layout.uc_activity_member_setting);
        initPreference();
        mAppContext.accountHelper().registerLoginStatusChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        mAppContext.accountHelper().unRegisterLoginStatusChangeListener(this);
        super.onDestroy();
        //ChameleonColorManager.getInstance().onDestroy(this);
    }

    private void initVariables() {
        mPresenter = new MemberSettingPresenter(this, this);
    }

    private void initAppContext() {
        if (SdkUtil.isCallBySdk(this)) {
            mAppContext = AmigoServiceSdk.getInstance().appContext();
        } else {
            mAppContext = (IAppContext) this.getApplication();
        }

    }

    private void initPreference() {
        mPushView = (AmigoSwitchPreference) findPreference(PREF_KEY_PUSH);
        mPushView.setChecked(isNotifySwitchEnable(this));
        mPushView.setOnPreferenceChangeListener(new AmigoPreference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(AmigoPreference amigoPreference, Object obj) {
                setNotifySwitchEnable(MemberSettingActivity.this, (Boolean) obj);
                return true;
            }
        });
        mFeedbackView = findPreference(PREF_KEY_FEEDBACK);
        mFeedbackView.setOnPreferenceClickListener(new AmigoPreference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(AmigoPreference amigoPreference) {
                if (PhoneUtil.isRunningByMonkey()) {
                    return false;
                }

                startFeedback();
                return false;
            }
        });

        mBtnChangeAccount = (Button) findViewById(R.id.btn_member_setting_change_account);
        mBtnChangeAccount.setBackground(getDrawable(R.drawable.round_cornor_button));
        mBtnChangeAccount.setOnClickListener(this);
        addChameleonColorView();
    }

    private void addChameleonColorView() {
        /*if (ChameleonColorManager.isNeedChangeColor()) {
            this.findViewById(R.id.layout_member_setting).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
            this.findViewById(R.id.layout_member_settings).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
        }*/
    }

    private void initActionbar() {
        AmigoActionBar mActionBar = getAmigoActionBar();
        if (mActionBar == null) {
            LogUtil.e(TAG, "getAmigoActionBar is null");
            return;
        }
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setTitle(R.string.uc_txt_member_card_setting);
    }

    public static boolean isNotifySwitchEnable(Context context) {
        SharedPrefHelper sp = SharedPrefHelper.getInstance(context);
        return sp.getBoolean(SHARE_KEY_NOTIFY_ENABLE, true);
    }

    public static void setNotifySwitchEnable(Context context, boolean enable) {
        SharedPrefHelper sp = SharedPrefHelper.getInstance(context);
        sp.putBoolean(SHARE_KEY_NOTIFY_ENABLE, enable);
        StatisticsUtil.onEvent(context, enable ? StatisticsEvents.Settings.NOTICE_SWICH_ON : StatisticsEvents.Settings.NOTICE_SWICH_OFF);
    }

    @Override
    public void onClick(View v) {
        if (!RepeatClickUtil.canRepeatClick(v)) {
            return;
        }
        if (v.getId() == R.id.btn_member_setting_change_account) {
            mPresenter.logout();
            StatisticsUtil.onEvent(this, StatisticsEvents.Settings.LOGOUT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GioneeAccount gioneeAccount = GioneeAccount.getInstance(getApplicationContext());
        gioneeAccount.onActivityResult(requestCode, resultCode, data);
    }

    private void startFeedback() {
        try {
            FeedbackApi.createFeedbackApi(this).gotoFeedback(this);
            StatisticsUtil.onEvent(this, StatisticsEvents.Settings.FEEDBACK);
        } catch (FeedBackException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetLoginStatus(boolean isLogin) {
        if (isLogin) {
            LogUtil.e(TAG, "onGetLoginStatus : isLogin is true!");
            return;
        }

        if (isFinishing() || isDestroyed()) {
            LogUtil.e(TAG, "onGetLoginStatus : activity is finishing.");
            return;
        }
        LogUtil.e(TAG, "onGetLoginStatus : exit activity!");
        finish();
    }

    @Override
    public void startLogout() {
        mBtnChangeAccount.setEnabled(false);
    }

    @Override
    public void logoutSuccess() {
        mBtnChangeAccount.setEnabled(true);
        finish();
    }

    @Override
    public void logoutError() {
        mBtnChangeAccount.setEnabled(true);
    }
}
