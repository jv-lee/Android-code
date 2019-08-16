package com.gionee.gnservice.sdk;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gionee.account.sdk.itf.vo.LoginInfo;
import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.entity.MemberPrivilege;
import com.gionee.gnservice.sdk.cardview.IMemberCardViewContract;
import com.gionee.gnservice.statistics.StatisticsEvents;
import com.gionee.gnservice.statistics.StatisticsUtil;
import com.gionee.gnservice.utils.ChameleonColorManager;
import com.gionee.gnservice.utils.ImageOOMUtil;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PhoneUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;
import com.gionee.gnservice.utils.RepeatClickUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.SdkUtil;
import com.gionee.gnservice.utils.ToastUtil;
import com.gionee.gnservice.widget.DrawableCenterButton;
import com.gionee.gnservice.widget.RotatableLayout;

import java.util.List;

import static com.gionee.gnservice.utils.ResourceUtil.getDrawableId;
import static com.gionee.gnservice.utils.ResourceUtil.getLayoutId;
import static com.gionee.gnservice.utils.ResourceUtil.getWidgetId;

public final class AmigoServiceCardView extends FrameLayout implements IMemberCardViewContract.View, View.OnClickListener {
    private static final String TAG = AmigoServiceCardView.class.getSimpleName();
    //会员卡
    private View mUnLoginLayout, mLoginFrontLayout, mLoginBackLayout, mUnloginBg, mLoginFrontBg, mLoginBackBg;
    private RotatableLayout mLoginRotateLayout;
    private TextView mTxtPhoneNumber;
    private DrawableCenterButton mBtnMemberCenter;
    private View mLayoutBtnMemerCenter;
    private TextView mTxtNickName, mTxtWarranty, mTxtPrivilegeNum, mTxtIntegral, mTxtAcoin, mTxtCoupon, mTxtCouponTitle, mTxtIntegralTitle, mTxtAcoinTitle;
    private IAppContext mAppContext;
    private String mIntegral, mAcoin, mCoupon, mPrivilegeSize, mWarranty;
    private AccountInfo mAccountInfo;
    private View mBackground;
    private View mLoadingView;
    private IAmigoServiceSdk.OnHandleListener mLoginStatusListener;
    private boolean mIsLogin = false;
    private OnGetIntegralUserListener mOnGetIntegralUserListener;

    public AmigoServiceCardView(Context context) {
        this(context, null);
    }

    public AmigoServiceCardView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public AmigoServiceCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context);
        initView(context);
        changeColor(context);
    }

    private void changeColor(Context context) {
        if ((!SdkUtil.isCallBySdk(context) || AmigoServiceSdk.getInstance().isSupportPowerMode())
                && ChameleonColorManager.isPowerSavingMode()) {
            if (ChameleonColorManager.getBackgroudColor_B1() != -1) {
                mBackground.setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBtnMemberCenter.setBackgroundResource(getDrawableId(context, "uc_member_card_view_btn_save_mode"));
            }
            Drawable drawable = getResources().getDrawable(getDrawableId(context, "uc_ic_member_card_view_btn_see_member_save_mode"));
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mBtnMemberCenter.setCompoundDrawables(drawable, null, null, null);
            mBtnMemberCenter.setTextColor(ResourceUtil.getColor(context, "uc_txt_member_card_view_btn_text_color_dark"));
        }
    }

    private void initAttributes(Context context) {
        mAppContext = AmigoServiceSdk.getInstance().appContext();
        mPrivilegeSize = mIntegral = mAcoin = mCoupon = "";
    }

    private void initView(Context context) {
        View view = inflate(context, getLayoutId(context, "uc_include_member_card_view"), this);
        PreconditionsUtil.checkNotNull(view);
        mLoadingView = getView(view, getWidgetId(context, "img_member_card_view_loading"));
        mLoadingView.setBackground(new BitmapDrawable(getResources(),
                ImageOOMUtil.getBitmap(getContext(), ResourceUtil.getDrawableId(getContext(), "uc_bg_member_card_view_loading"))));
        mUnLoginLayout = getView(view, getWidgetId(context, "llayout_member_card_view_account_unlogin_background"));

        mLoginRotateLayout = getView(view, getWidgetId(context, "rotatable_member_card_view_account_login"));
        mLoginRotateLayout.setOnClickListener(this);
        mLoginRotateLayout.setOnRotateListener(mOnRotateListener);
        mLoginFrontLayout = getView(view, getWidgetId(context, "llayout_member_card_view_account_login_front_background"));
        mLoginBackLayout = getView(view, getWidgetId(context, "llayout_member_card_view_account_login_back_background"));

        mUnloginBg = getView(view, getWidgetId(context, "layout_member_card_view_account_unlogin"));
        mLoginFrontBg = getView(view, getWidgetId(context, "llayout_member_card_view_account_login_front"));
        mLoginBackBg = getView(view, getWidgetId(context, "rlayout_member_card_view_account_login_back"));

        mLayoutBtnMemerCenter = getView(view, getWidgetId(context, "layout_member_card_view_member"));
        mBtnMemberCenter = getView(view, getWidgetId(context, "btn_member_card_view_member"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBtnMemberCenter.setBackgroundResource(getDrawableId(context, "uc_member_card_view_btn_ripple"));
        }
        mBtnMemberCenter.setOnClickListener(this);

        getView(view, getWidgetId(context, "img_member_card_view_account_unlogin_photo")).setOnClickListener(this);
        getView(view, getWidgetId(context, "txt_member_card_view_account_unlogin_register")).setOnClickListener(this);
        getView(view, getWidgetId(context, "txt_member_card_view_account_unlogin_login")).setOnClickListener(this);

        mTxtPhoneNumber = getView(view, getWidgetId(context, "txt_member_card_view_account_login_phone_number"));
        mTxtNickName = getView(view, getWidgetId(context, "txt_member_card_view_account_login_back_nickname"));
        mTxtWarranty = getView(view, getWidgetId(context, "txt_member_card_view_account_login_back_warranty"));
        mTxtPrivilegeNum = getView(view, getWidgetId(context, "txt_member_card_view_account_login_back_privilege"));
        mTxtIntegral = getView(view, getWidgetId(context, "txt_member_card_view_account_login_back_integral"));
        mTxtAcoin = getView(view, getWidgetId(context, "txt_member_card_view_account_login_back_acoin"));
        mTxtCoupon = getView(view, getWidgetId(context, "txt_member_card_view_account_login_back_coupon"));

        mTxtIntegralTitle = getView(view, getWidgetId(context, "txt_member_card_view_account_login_back_integral_title"));
        mTxtAcoinTitle = getView(view, getWidgetId(context, "txt_member_card_view_account_login_back_acoin_title"));
        mTxtCouponTitle = getView(view, getWidgetId(context, "txt_member_card_view_account_login_back_coupon_title"));
        mBackground = getView(view, getWidgetId(context, "llayout_member_card_view_account"));
    }

    @Override
    public void showUserInfo(AccountInfo info) {
        mAccountInfo = info;
        mTxtPhoneNumber.setText(PhoneUtil.formatPhoneNumber(mAccountInfo.getUserName()));
        updateStateView(true);
        autoRotate();
    }

    @Override
    public void showPhoto(AccountInfo info) {

    }

    public boolean isLogin() {
        return mIsLogin;
    }

    public void setLoginStatusListener(IAmigoServiceSdk.OnHandleListener listener) {
        mLoginStatusListener = listener;
    }

    @Override
    public void showMemberLevel(MemberLevel level) {
        showMemberLevelView(level);
    }

    @Override
    public void showNickName(AccountInfo info) {
        if (!TextUtils.isEmpty(info.getNickName())) {
            mTxtNickName.setText(info.getNickName());
            if (mAccountInfo != null) {
                mAccountInfo.setNickName(info.getNickName());
            }
            return;
        }
        if (mAccountInfo != null && !TextUtils.isEmpty(mAccountInfo.getUserName())) {
            mTxtNickName.setText(PhoneUtil.formatPhoneNumber(mAccountInfo.getUserName()));
            return;
        }
        // TODO: 17-11-4 debug for 251471:set default nick name
        LogUtil.i(TAG, "something wrong to set nick name.");
        mTxtNickName.setText("");
    }

    private void updateStateView(boolean isLogin) {
        mLoadingView.setVisibility(GONE);
        mIsLogin = isLogin;
        if (isLogin) {
            mLoginRotateLayout.setVisibility(VISIBLE);
            mLayoutBtnMemerCenter.setVisibility(VISIBLE);
            mUnLoginLayout.setVisibility(GONE);
        } else {
            mUnLoginLayout.setVisibility(VISIBLE);
            mLoginRotateLayout.setVisibility(GONE);
            mLayoutBtnMemerCenter.setVisibility(GONE);
        }
    }

    private void showShadowView() {
        mUnLoginLayout.setBackground(new BitmapDrawable(getResources(),
                ImageOOMUtil.getBitmap(getContext(), ResourceUtil.getDrawableId(getContext(), "uc_bg_member_card_view_shadow_unlogin"))));

        mLoginFrontLayout.setBackground(new BitmapDrawable(getResources(),
                ImageOOMUtil.getBitmap(getContext(), ResourceUtil.getDrawableId(getContext(), "uc_bg_member_card_view_shadow_login"))));

        mLoginBackLayout.setBackground(new BitmapDrawable(getResources(),
                ImageOOMUtil.getBitmap(getContext(), ResourceUtil.getDrawableId(getContext(), "uc_bg_member_card_view_shadow_login"))));
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(View view, int id) {
        return (T) view.findViewById(id);
    }

    @Override
    public void onClick(View v) {
        // FIXME: 17-10-11 debug for 229903
        LogUtil.d(TAG, "onClick");
        if (!RepeatClickUtil.canRepeatClick(v)) {
            LogUtil.d(TAG, "repeat click,return");
            return;
        }
        Context context = v.getContext();
        int id = v.getId();
        if (id == getWidgetId(context, "btn_member_card_view_member")) {
            // FIXME: 17-10-11 debug for 229903
            LogUtil.d(TAG, "click start member card activity");
            startMemberCardActivity(context);
        } else if (id == getWidgetId(context, "rotatable_member_card_view_account_login")) {
            startReversalView();
        } else {
            if (id == getWidgetId(context, "txt_member_card_view_account_unlogin_login")) {
                startAccountActivity(v.getContext());
                StatisticsUtil.onEvent(this.getContext(), StatisticsEvents.Main.LOGIN);
                return;
            }
            if (id == getWidgetId(context, "txt_member_card_view_account_unlogin_register")) {
                StatisticsUtil.onEvent(this.getContext(), StatisticsEvents.Main.REGISTER);
                startAccountActivity(v.getContext());
                return;
            }
            if (id == getWidgetId(context, "img_member_card_view_account_unlogin_photo")) {
                startAccountActivity(v.getContext());
            }

        }
    }

    // 翻转页面
    private void startReversalView() {
        StatisticsUtil.onEvent(this.getContext(), StatisticsEvents.Main.ROLL_MEMBER_CARD);
        mLoginRotateLayout.startRotate();
    }

    private void startMemberCardActivity(Context context) {
        Intent intent = new Intent();
        //from AmigoServiceapp
        if (context.getPackageName().equals(SdkUtil.AMIGO_SERVICE_PACKAGE_NAME)) {
            LogUtil.d(TAG, "from app");
            intent.setClassName(context, "com.gionee.gnservice.sdk.member.MemberCardActivity");
            intent.setPackage(SdkUtil.AMIGO_SERVICE_PACKAGE_NAME);
        } else {
            if (SdkUtil.hasSupportAmigoServiceVersion(context)) {
                LogUtil.d(TAG, "has support Amigo Servcie version");
                intent.setAction("com.gionee.gnservice.sdk.toMemberCardActivity");
                intent.setPackage(SdkUtil.AMIGO_SERVICE_PACKAGE_NAME);
            } else if (SdkUtil.isSdkCommonVersion()) {
                LogUtil.d(TAG, "have no right version Amigo_Servcie,use common sdk");
                intent.setClassName(context, "com.gionee.gnservice.sdk.member.MemberCardActivity");
                intent.setPackage(context.getPackageName());
            } else {
                throw new RuntimeException("please use common sdk");
            }
        }

        intent.putExtra("accountInfo", mAccountInfo);
        intent.putExtra("integral", mIntegral);
        intent.putExtra("acoin", mAcoin);
        intent.putExtra("coupon", mCoupon);
        intent.putExtra("privilegeSize", mPrivilegeSize);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // FIXME: 17-10-11 debug for 229903
            LogUtil.d(TAG, "really start member card activity");
            context.startActivity(intent);
            StatisticsUtil.onEvent(this.getContext(), StatisticsEvents.Main.MEMBER_CENTER);
        } else {
            throw new RuntimeException("intent is not exist!");
        }

    }

    private void startAccountActivity(final Context context) {
        IAmigoServiceSdk.OnHandleListener listener;
        if (mLoginStatusListener != null) {
            listener = mLoginStatusListener;
        } else {
            listener = new IAmigoServiceSdk.OnHandleListener() {
                @Override
                public void onSuccess(LoginInfo loginInfo) {

                }

                @Override
                public void onFail() {
//                    ToastUtil.showLong(context, getStringId(context, "uc_network_exception"));
                }

                @Override
                public void onCancel() {

                }
            };
        }

        mAppContext.accountHelper().login(listener);
    }

    private void showMemberLevelView(MemberLevel memberLevel) {
        Context context = getContext();
        if (memberLevel == MemberLevel.BLACK_GOLD) {
            Resources res = context.getResources();
            mTxtNickName.setTextColor(res.getColor(ResourceUtil.getColorId(context, "uc_txt_member_card_view_username_black_gold")));
            mTxtWarranty.setTextColor(res.getColor(ResourceUtil.getColorId(context, "uc_txt_member_card_view_warranty_black_gold")));
            mTxtPrivilegeNum.setTextColor(res.getColor(ResourceUtil.getColorId(context, "uc_txt_member_card_view_warranty_black_gold")));
            mTxtCouponTitle.setTextColor(res.getColor(ResourceUtil.getColorId(context, "uc_txt_member_card_view_integral_black_gold")));
            mTxtAcoinTitle.setTextColor(res.getColor(ResourceUtil.getColorId(context, "uc_txt_member_card_view_integral_black_gold")));
            mTxtIntegralTitle.setTextColor(res.getColor(ResourceUtil.getColorId(context, "uc_txt_member_card_view_integral_black_gold")));
        } else {
            Resources res = context.getResources();
            mTxtNickName.setTextColor(res.getColor(ResourceUtil.getColorId(context, "uc_txt_member_card_view_username")));
            mTxtWarranty.setTextColor(res.getColor(ResourceUtil.getColorId(context, "uc_txt_member_card_view_warranty")));
            mTxtPrivilegeNum.setTextColor(res.getColor(ResourceUtil.getColorId(context, "uc_txt_member_card_view_warranty")));
            mTxtCouponTitle.setTextColor(res.getColor(ResourceUtil.getColorId(context, "uc_txt_member_card_view_integral")));
            mTxtAcoinTitle.setTextColor(res.getColor(ResourceUtil.getColorId(context, "uc_txt_member_card_view_integral")));
            mTxtIntegralTitle.setTextColor(res.getColor(ResourceUtil.getColorId(context, "uc_txt_member_card_view_integral")));
        }
    }

    @Override
    public void showUnLoginView(MemberLevel level) {
        LogUtil.d(TAG, "show Unlogin view");
        updateStateView(false);
        if (level != null) {
            showMemberLevelView(level);
        }
    }

    @Override
    public void showWarrantyInfo(String warranty) {
        LogUtil.d(TAG, "load warranty info is" + warranty);
        if (!TextUtils.isEmpty(warranty)) {
            String value = Integer.parseInt(warranty) == 1 ? ResourceUtil.getString(getContext(), "uc_txt_member_card_view_warranty_no") :
                    ResourceUtil.getString(getContext(), "uc_txt_member_card_view_warranty_yes");
            mTxtWarranty.setText(value);
            mWarranty = warranty;
        }
    }

    @Override
    public void showPrivilegeView(List<MemberPrivilege> memberPrivilegeList) {
        if (memberPrivilegeList != null && !memberPrivilegeList.isEmpty()) {
            mPrivilegeSize = String.valueOf(memberPrivilegeList.size());
            mTxtPrivilegeNum.setText(String.format(ResourceUtil.getString(getContext(), "uc_txt_member_card_view_privilege_num"), memberPrivilegeList.size()));
        }
    }

    @Override
    public void showCouponNumView(String value) {
        mCoupon = value;
        mTxtCoupon.setText(value);
    }

    private void autoRotate() {
        boolean firstEnter = mAppContext.sharedPrefHelper().getBoolean("first_enter", true);
        if (firstEnter && !SdkUtil.isCallBySdk(getContext())) {
            startReversalView();
        }
    }

    @Override
    public void showAcoinView(String acoin) {
        LogUtil.d(TAG, "show acoin is:" + acoin);
        mAcoin = acoin;
        mTxtAcoin.setText(acoin);
    }

    @Override
    public void showIntegralUser(String value) {
        mIntegral = value;
        mTxtIntegral.setText(value);
        if (mOnGetIntegralUserListener != null) {
            mOnGetIntegralUserListener.onGetIntegralUser(value);
        }
        //hclw不显示积分
        mTxtIntegral.setVisibility(View.GONE);
        mTxtIntegralTitle.setVisibility(View.GONE);
    }

    public interface OnGetIntegralUserListener {
        void onGetIntegralUser(String value);

    }

    public void setOnGetIntegralUserListener(OnGetIntegralUserListener listener) {
        mOnGetIntegralUserListener = listener;
    }

    @Override
    public void showNetErrorView() {
        ToastUtil.showNetWorkErrorToast(getContext());
    }

    @Override
    public void showMemberCardImg(List<Bitmap> bitmaps) {
        LogUtil.d(TAG, "showMemberCardImg");
        if (bitmaps != null && bitmaps.size() == 3) {
            mUnloginBg.setBackground(new BitmapDrawable(getResources(), bitmaps.get(0)));
            mLoginFrontBg.setBackground(new BitmapDrawable(getResources(), bitmaps.get(1)));
            mLoginBackBg.setBackground(new BitmapDrawable(getResources(), bitmaps.get(2)));
            showShadowView();
        }
    }

    private RotatableLayout.OnRotateListener mOnRotateListener = new RotatableLayout.OnRotateListener() {
        @Override
        public void onRotated(boolean isFront) {
            if (!isFront) {
                if (!TextUtils.isEmpty(mIntegral)) {
                    ValueAnimator animator = getValueAnimator(mTxtIntegral, 0, Integer.valueOf(mIntegral), 400);
                    animator.start();
                }

                if (!TextUtils.isEmpty(mAcoin)) {
                    ValueAnimator animator = getValueAnimator(mTxtAcoin, 0, Math.round(new Float(mAcoin)), 400);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (mTxtAcoin != null && mAcoin != null) {
                                mTxtAcoin.setText(mAcoin);
                            }
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {}

                        @Override
                        public void onAnimationCancel(Animator animation) {}

                        @Override
                        public void onAnimationRepeat(Animator animation) {}
                    });
                    animator.start();
                }

                if (!TextUtils.isEmpty(mCoupon)) {
                    ValueAnimator animator = getValueAnimator(mTxtCoupon, 0, Integer.valueOf(mCoupon), 400);
                    animator.start();
                }

                boolean firstEnter = mAppContext.sharedPrefHelper().getBoolean("first_enter", true);
                if (firstEnter && !SdkUtil.isCallBySdk(getContext())) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startReversalView();
                    mAppContext.sharedPrefHelper().putBoolean("first_enter", false);
                }
            } else {
                mTxtIntegral.setText("");
                mTxtAcoin.setText("");
                mTxtCoupon.setText("");
            }
        }
    };

    @TargetApi(23)
    private ValueAnimator getValueAnimator(final TextView textView, int min, int max, long duration) {
        final ValueAnimator animator = ValueAnimator.ofInt(min, max);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animator.getAnimatedValue();
                textView.setText(String.valueOf(value));
            }
        });
        return animator;
    }
}
