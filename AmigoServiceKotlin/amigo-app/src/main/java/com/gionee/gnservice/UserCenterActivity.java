package com.gionee.gnservice;

import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import com.gionee.account.sdk.itf.vo.LoginInfo;
import com.gionee.gnservice.base.BaseActivity;
import com.gionee.gnservice.base.BaseRecyclerViewAdapter;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.entity.PrivilegeAction;
import com.gionee.gnservice.entity.ServiceInfo;
import com.gionee.gnservice.module.integral.MemberIntegralMakeActivity;
import com.gionee.gnservice.module.main.*;
import com.gionee.gnservice.module.main.banner.Banner;
import com.gionee.gnservice.module.main.banner.BannerImageLoader;
import com.gionee.gnservice.module.main.banner.OnBannerListener;
import com.gionee.gnservice.module.main.mvp.IUserCenterContract;
import com.gionee.gnservice.module.main.mvp.UserCenterPresenter;
import com.gionee.gnservice.module.setting.push.PushHelper;
import com.gionee.gnservice.sdk.AmigoServiceCardView;
import com.gionee.gnservice.sdk.AmigoServiceSdk;
import com.gionee.gnservice.sdk.IAmigoServiceSdk;
import com.gionee.gnservice.sdk.integral.MemberIntegralLotteryActivity;
import com.gionee.gnservice.sdk.integral.MemberIntegralMallActivity;
import com.gionee.gnservice.sdk.integral.TextUpDownScroll;
import com.gionee.gnservice.sdk.integral.mvp.IMemberIntegralContract;
import com.gionee.gnservice.sdk.integral.mvp.MemberIntegralPresenter;
import com.gionee.gnservice.statistics.StatisticsEvents;
import com.gionee.gnservice.statistics.StatisticsUtil;
import com.gionee.gnservice.utils.*;
import com.gionee.gnservice.widget.AdapterGridLayoutManager;
import com.gionee.gnservice.widget.AdapterRecyclerView;
import com.gionee.gnservice.widget.AutoVerticalScrollTextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserCenterActivity extends BaseActivity implements IUserCenterContract.View, IMemberIntegralContract.View {
    private static final String TAG = UserCenterActivity.class.getSimpleName();
    private IUserCenterContract.Presenter mPresenter;
    //权限申请
    private PermissionHandler mPermissionHandler;
    //权限弹框
    private PermissionDialogHandler mPermissionDialogHandler;

    //会员特权
    private View mLayoutBanner;
    private Banner mBannerPrivileges;
    private List<PrivilegeAction> mPrivilegeActions;

    //会员积分
    private View mLayoutPrize;
    private AutoVerticalScrollTextView mTxtPrize;
    private TextUpDownScroll mPrizeScroll;
    private IMemberIntegralContract.Presenter mIntegralPresenter;
    private TextView mTxtIntegralLottery, mTxtIntegralMall, mTxtIntegralMake;

    //会员服务
    private AdapterRecyclerView mRecyclerServices;
    private RecyclerViewServicesAdapter mRecyclerServicesAdapter;
    private List<ServiceInfo> mServiceInfoList;
    private AmigoServiceCardView mAmigoServiceCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //权限申请
        mPermissionHandler = new PermissionHandler(this);
        mPermissionHandler.reqeustPermissions();
        SdkUtil.setFromSdkDemo(false);
        super.onCreate(savedInstanceState);
        if (AppConfig.DEBUG && AppConfig.TRACE_DEBUG) {
            Debug.startMethodTracing("service");

        }
        PushHelper.registerPushRid(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AmigoServiceSdk.getInstance().onResume(mAmigoServiceCardView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (AppConfig.DEBUG && AppConfig.TRACE_DEBUG) {
            Debug.stopMethodTracing();
        }
        if (mPrizeScroll != null) {
            mPrizeScroll.endScroll();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.uc_activity_user_center;
    }

    @Override
    protected String getActionbarTitle() {
        return getString(R.string.uc_user_center);
    }

    @Override
    protected void initVariables() {
        mPermissionDialogHandler = new PermissionDialogHandler(this, appContext());
        mPresenter = new UserCenterPresenter(appContext(), this);
        mIntegralPresenter = new MemberIntegralPresenter(appContext(), this);
        mServiceInfoList = new ArrayList<ServiceInfo>();
    }

    @Override
    protected void initData() {
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.loadServiceInfo();
                        mPresenter.loadPrivilegesAdds();
                        mIntegralPresenter.loadIntegralPrizes();
                    }
                });
            }
        });
    }

    private boolean isLogin() {
        return mAmigoServiceCardView.isLogin();
    }

    private void checkLogin(final OnCheckLoginListener listener) {
        if (PhoneUtil.isRunningByMonkey()) {
            return;
        }

        if (listener == null) {
            LogUtil.e(TAG, "login listener should not be null");
            return;
        }

        if (isLogin()) {
            listener.onLogin();
        } else {
            appContext().accountHelper().login(new IAmigoServiceSdk.OnHandleListener() {
                @Override
                public void onSuccess(LoginInfo loginInfo) {
                    listener.onLogin();
                }

                @Override
                public void onFail() {
                    LogUtil.e(TAG, "login account error");
                }

                @Override
                public void onCancel() {}
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mPermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            LogUtil.e(TAG, "getActionBar return null");
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    /*@Override
    protected void addChameleonColorView() {
        getView(R.id.layout_user_center).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
        getView(R.id.layout_user_center_privilege_bannar).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
        getView(R.id.llayout_user_center_integral).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
        getView(R.id.layout_user_center_service).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
    }*/

    @Override
    protected void initView() {
        initMemberCardView();
        initMemberPrivilegeView();
        initIntegralView();
        initServiceView();
    }

    private void initMemberCardView() {
        mAmigoServiceCardView = getView(R.id.uc_member_card_view);
        mAmigoServiceCardView.setOnGetIntegralUserListener(new AmigoServiceCardView.OnGetIntegralUserListener() {
            @Override
            public void onGetIntegralUser(String value) {
                showIntegralUserView(Integer.valueOf(value));
            }
        });
    }

    private void initMemberPrivilegeView() {
        mBannerPrivileges = getView(R.id.banner_user_center_privilege);
        //mBannerPrivileges.isAutoPlay(false);
        mLayoutBanner = getView(R.id.layout_user_center_privilege_bannar);
    }

    private void initIntegralView() {
        mLayoutPrize = getView(R.id.layout_user_center_integral_prize);
        mTxtPrize = getView(R.id.txt_user_center_integral_prize);

        mTxtIntegralLottery = getView(R.id.txt_user_center_integral_lottery_content);
        mTxtIntegralMake = getView(R.id.txt_user_center_integral_make_content);
        mTxtIntegralMall = getView(R.id.txt_user_center_integral_mall_content);

        View layoutIntegralMall = getView(R.id.llayout_user_center_integral_mall);
        View layoutIntegralLottery = getView(R.id.llayout_user_center_integral_lottery);
        View layoutIntegralMake = getView(R.id.llayout_user_center_integral_make);

        layoutIntegralMall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RepeatClickUtil.canRepeatClick(v)) {
                    return;
                }

                checkLogin(new OnCheckLoginListener() {
                    @Override
                    public void onLogin() {
                        startMemberIntegralMall();
                    }
                });
            }
        });

        layoutIntegralLottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RepeatClickUtil.canRepeatClick(v)) {
                    return;
                }

                checkLogin(new OnCheckLoginListener() {
                    @Override
                    public void onLogin() {
                        startMemberIntegralLottery();
                    }
                });
            }
        });

        layoutIntegralMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RepeatClickUtil.canRepeatClick(v)) {
                    return;
                }

                checkLogin(new OnCheckLoginListener() {
                    @Override
                    public void onLogin() {
                        startMemberIntegralMake();
                    }
                });
            }
        });
    }

    private void initServiceView() {
        mRecyclerServices = getView(R.id.recyleview_user_center_services);
        mRecyclerServices.setHasFixedSize(true);
        mRecyclerServices.setLayoutManager(new AdapterGridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mRecyclerServicesAdapter = new RecyclerViewServicesAdapter(this, mServiceInfoList);
        mRecyclerServicesAdapter.setOnItemClickListener(
                new BaseRecyclerViewAdapter.OnItemClickListener<ServiceInfo>() {
                    @Override
                    public void onItemClick(ServiceInfo moduleInfo, int position) {
                        StatisticsUtil.onEvent(UserCenterActivity.this.getApplicationContext(), moduleInfo.getName());
                        onRecyclerModulesItemClick(moduleInfo);
                    }
                });
        mRecyclerServices.setAdapter(mRecyclerServicesAdapter);
    }

    //------------------------会员卡信息-----------------------------//


    //------------------------会员特权-----------------------------//
    private OnBannerListener mOnBannerPrivilegeListener = new OnBannerListener() {
        @Override
        public void OnBannerClick(int position) {
            LogUtil.d(TAG, "on Banner click,position is:" + position);
            final PrivilegeAction action = mPrivilegeActions.get(position);
            checkLogin(new OnCheckLoginListener() {
                @Override
                public void onLogin() {
                    Intent intent = new Intent();
                    switch (action.getType()) {
                        case PrivilegeAction.TYPE_WEB:
                            intent.setClass(UserCenterActivity.this, WebViewActivity.class);
                            intent.putExtra("url", action.getContent());
                            break;

                        case PrivilegeAction.TYPE_START_APP:
                            String content = action.getContent();
                            if (isJson(content)) {
                                String packageName = null;
                                String action = null;
                                try {
                                    JSONObject jo = new JSONObject(content);
                                    action = jo.getString("action");
                                    packageName = jo.getString("package");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                LogUtil.d(TAG, "banner item click, " + "pkg is:" + packageName + "; action is:" + action);
                                if (!TextUtils.isEmpty(packageName)) {
                                    intent.setPackage(packageName);
                                }
                                if (!TextUtils.isEmpty(action)) {
                                    intent.setAction(action);
                                }
                            } else {
                                intent.setAction(content);
                            }
                            break;

                        default:
                            break;
                    }
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                        String eventId = action.getId() + "-" + action.getName();
                        StatisticsUtil.onEvent(UserCenterActivity.this, eventId, StatisticsEvents.Label.BANNER);
                    } else {
                        LogUtil.e(TAG, "Activity is not exist");
                    }
                }
            });
        }
    };

    private boolean isJson(String jsonStr) {
        if (jsonStr == null) {
            return false;
        }
        return jsonStr.startsWith("{") && jsonStr.endsWith("}");
    }

    @Override
    public void showPrivilegeAddsView(List<PrivilegeAction> privilegeActions) {
        mPrivilegeActions = privilegeActions;
        if (privilegeActions == null || privilegeActions.isEmpty()) {
            LogUtil.i(TAG, "showPrivilegeAddsView() no privilegeActions");
            return;
        }
        mLayoutBanner.setVisibility(View.VISIBLE);
        getView(R.id.uc_user_center_integral_view).setVisibility(View.VISIBLE);
        List<String> imgUrls = new ArrayList<String>();
        for (PrivilegeAction action : privilegeActions) {
            imgUrls.add(action.getImageUrl());
        }
        mBannerPrivileges.setImages(imgUrls)
                .setImageLoader(new BannerImageLoader())
                .setOnBannerListener(mOnBannerPrivilegeListener)
                .start();
    }

    //------------------------会员积分-----------------------------//
    private void startMemberIntegralMake() {
        Intent intent = new Intent(UserCenterActivity.this, MemberIntegralMakeActivity.class);
        try {
            startActivity(intent);
            StatisticsUtil.onEvent(this, StatisticsEvents.Main.GAIN_SCORE, StatisticsEvents.Label.MEMBER_CENTER);
        } catch (ActivityNotFoundException e) {
            LogUtil.e(TAG, "not found MemberIntegralMakeActivity");
        }
    }

    private void startMemberIntegralMall() {
        Intent intent = new Intent(UserCenterActivity.this, MemberIntegralMallActivity.class);
        try {
            startActivity(intent);
            StatisticsUtil.onEvent(this, StatisticsEvents.Main.SCORE_MALL, StatisticsEvents.Label.MEMBER_CENTER);
        } catch (ActivityNotFoundException e) {
            LogUtil.e(TAG, "not found MemberIntegralMallActivity");
        }
    }

    private void startMemberIntegralLottery() {
        Intent intent = new Intent(UserCenterActivity.this, MemberIntegralLotteryActivity.class);
        try {
            startActivity(intent);
            StatisticsUtil.onEvent(this, StatisticsEvents.Main.LOTTERY, StatisticsEvents.Label.MEMBER_CENTER);
        } catch (ActivityNotFoundException e) {
            LogUtil.e(TAG, "not found MemberIntegralLotteryActivity");
        }
    }

    @Override
    public void showLotteryRemainTimeView(int times) {
        mIntegralPresenter.loadIntegralMakeTasksNumber(null);
        if (times == 0) {
            mTxtIntegralLottery.setText(ResourceUtil.getString(this, "uc_txt_member_integral_lottery_desription_no_time"));
        } else {
            String value = String.format(ResourceUtil.getString(this, "uc_user_center_member_integral_lottery_description"), String.valueOf(times));
            mTxtIntegralLottery.setText(formatSpannableString(value, String.valueOf(times)));
        }
    }

    @Override
    public void showIntegralPrizeView(List<String> prizeList) {
        if (prizeList == null) {
            return;
        }

        if (prizeList.isEmpty()) {
            return;
        }

        mLayoutPrize.setVisibility(View.VISIBLE);
        mPrizeScroll = new TextUpDownScroll(mTxtPrize, prizeList);
        mPrizeScroll.startScroll();
    }

    @Override
    public void showIntegralUserView(int value) {
        mIntegralPresenter.loadLotteryRemainTimes(null);
        String string = String.format(getString(R.string.uc_user_center_member_integral_mall_description), String.valueOf(value));
        mTxtIntegralMall.setText(formatSpannableString(string, String.valueOf(value)));
    }

    @Override
    public void showIntegralMakeTasksNumber(int taskSize, int integralCanGet) {
        CharSequence string = null;
        if (taskSize != 0) {
            String value = String.format(getString(R.string.uc_user_center_member_integral_make_description), String.valueOf(taskSize), String.valueOf(integralCanGet));
            string = formatSpannableString(value, String.valueOf(taskSize), String.valueOf(integralCanGet));
        } else {
            string = getString(R.string.uc_user_center_member_integral_make_description_empty);
        }
        mTxtIntegralMake.setText(string);
    }

    private CharSequence formatSpannableString(String value, String number) {
        int start = value.indexOf(number);
        int end = start + number.length();
        SpannableStringBuilder style = new SpannableStringBuilder(value);
        int color = getResources().getColor(R.color.uc_user_center_card_integral_num);
        style.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

    private CharSequence formatSpannableString(String value, String number1, String number2) {
        int start1 = value.indexOf(number1);
        int end1 = start1 + number1.length();
        int start2 = value.lastIndexOf(number2);
        int end2 = start2 + number2.length();
        SpannableStringBuilder style = new SpannableStringBuilder(value);
        int color = getResources().getColor(R.color.uc_user_center_card_integral_num);
        style.setSpan(new ForegroundColorSpan(color), start1, end1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(color), start2, end2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

    private void onRecyclerModulesItemClick(ServiceInfo moduleInfo) {
        if (PhoneUtil.isRunningByMonkey()) {
            return;
        }

        if (!mPermissionDialogHandler.showPermissionDialog(moduleInfo)) {
            IModule module = new BaseModule(this, moduleInfo);
            module.startModule();
        }
    }

    @Override
    public void showServiceView(List<ServiceInfo> serviceInfoList) {
        mServiceInfoList = serviceInfoList;
        mRecyclerServicesAdapter.setDatas(mServiceInfoList);
        mRecyclerServicesAdapter.notifyDataSetChanged();
    }

    private interface OnCheckLoginListener {
        void onLogin();
    }

}
