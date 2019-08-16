package com.gionee.gnservice.sdk.member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gionee.gnservice.base.BaseRecyclerViewAdapter;
import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.common.imageloader.ImageConfig;
import com.gionee.gnservice.common.imageloader.ImageLoaderImpl;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.entity.MemberLevelInfo;
import com.gionee.gnservice.entity.MemberPrivilege;
import com.gionee.gnservice.sdk.member.mvp.IMemberPrivilegeContract;
import com.gionee.gnservice.sdk.member.mvp.MemberPrivilegePresenter;
import com.gionee.gnservice.statistics.StatisticsEvents;
import com.gionee.gnservice.statistics.StatisticsUtil;
import com.gionee.gnservice.utils.ImageOOMUtil;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PreconditionsUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.ToastUtil;
import com.gionee.gnservice.widget.AdapterGridLayoutManager;
import com.gionee.gnservice.widget.UserLevelView;
import com.gionee.gnservice.widget.fresh.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import static com.gionee.gnservice.utils.ResourceUtil.getDrawableId;
import static com.gionee.gnservice.utils.ResourceUtil.getWidgetId;

public class MemberPrivilegeActivity extends BaseActionbarTransparentActivity implements IMemberPrivilegeContract.View {
    public static final String TAG = MemberPrivilegeActivity.class.getSimpleName();
    public static final String KEY_INTENT_PRIVILEGE = "key_intent_privilege";

    private View mLayoutBackground;
    private List<MemberPrivilege> mPrivilegeList;
    private IMemberPrivilegeContract.Presenter mPresenter;
    private UserLevelView mUserLevelPrivilege;
    private ImageView mImgPhoto;
    private ProgressBar mProgressExperience;
    private TextView mTxtProgress;
    private TextView mTxtUserLevel;
    private TextView mBtnAoin;

    private RecyclerView mRecyclePrivileges;
    private View mPrivilegeLoadingView;

    private RecyclePrivilegeAdapter mPrivilegeAdapter;
    private TwinklingRefreshLayout mRefreshLayout;

    @Override
    protected void initVariables() {
        mPrivilegeList = new ArrayList<MemberPrivilege>();
        mPresenter = new MemberPrivilegePresenter(appContext(), this);
    }

    @Override
    protected void initView() {
        mLayoutBackground = getView(getWidgetId(this, "layout_member_privilege"));
        mLayoutBackground.setBackground(new BitmapDrawable(getResources(), ImageOOMUtil.getBitmap(this, ResourceUtil.getDrawableId(this, "uc_bg_member_privilege_actionbar"))));
        mRefreshLayout = getView(getWidgetId(this, "refresh_member_privilege"));
        mRefreshLayout.setPureScrollModeOn();
        mImgPhoto = getView(getWidgetId(this, "img_member_privilege_user_photo"));
        mTxtUserLevel = getView(getWidgetId(this, "txt_member_privilege_user_level"));
        mProgressExperience = getView(getWidgetId(this, "progress_member_privilege_user_experience"));
        mTxtProgress = getView(getWidgetId(this, "txt_member_privilege_progress_user_experience"));
        mBtnAoin = getView(getWidgetId(this, "btn_member_privilege_acoin"));
        if (!showWallet()) {
            mBtnAoin.setVisibility(View.GONE);
        }
        mBtnAoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.gionee.gsp.wallet.components.activities.WalletMainActivity");
                intent.putExtra("app_id", AppConfig.Account.AMIGO_SERVICE_APP_ID);
                startActivity(intent);
                StatisticsUtil.onEvent(MemberPrivilegeActivity.this, StatisticsEvents.Main.CHARGE_FOR_ACOIN);
            }
        });
        mUserLevelPrivilege = getView(getWidgetId(this, "userlevel_member_privilege"));
        mRecyclePrivileges = getView(getWidgetId(this, "recycler_member_privilege_privileges"));
        mRecyclePrivileges.setLayoutManager(new AdapterGridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        mPrivilegeAdapter = new RecyclePrivilegeAdapter(appContext(), mPrivilegeList);
        mPrivilegeAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<MemberPrivilege>() {
            @Override
            public void onItemClick(MemberPrivilege memberPrivilege, int position) {
                onRecycleViewItemClicked(memberPrivilege, position);
            }
        });
        mRecyclePrivileges.setAdapter(mPrivilegeAdapter);
        mPrivilegeLoadingView = getView(getWidgetId(this, "progress_member_privilege_loading"));

        Intent intent = getIntent();
        AccountInfo accountInfo = (AccountInfo) intent.getSerializableExtra("accountInfo");

        Bitmap bitmap = intent.getParcelableExtra("photo");
        if (accountInfo != null) {
            LogUtil.i(TAG, "get account info, level = " + accountInfo.getMemberLevel());
            accountInfo.setPhoto(bitmap);
            showAccountView(accountInfo);
        }
    }

    public Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }


    private boolean showWallet() {
        Intent intent = new Intent("com.gionee.gsp.wallet.components.activities.WalletMainActivity");
        intent.putExtra("app_id", AppConfig.Account.getAmigoServiceAppId());
        return intent.resolveActivity(getPackageManager()) != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.loadAccountInfo();
            if (mPrivilegeList == null || mPrivilegeList.size() == 0) {
                showMemberPrivilegesLoadingView();
            }
        }
    }

    private void onRecycleViewItemClicked(MemberPrivilege memberPrivilege, int position) {
        Intent intent = new Intent();
        if (isMorePrivilege(memberPrivilege, position)) {
            intent.setClass(this, MemberPrivilegeContentMoreActivity.class);
        } else {
            intent.setClass(this, MemberPrivilegeContentActivity.class);
        }
        intent.putExtra(KEY_INTENT_PRIVILEGE, memberPrivilege);
        startActivity(intent);
        StatisticsUtil.onEvent(this, memberPrivilege.getName(), getLevelLabel(memberPrivilege));
    }

    private String getLevelLabel(MemberPrivilege memberPrivilege) {
        String eventLabel = "";
        switch (memberPrivilege.getMemberLevel()) {
            case GOLD:
                eventLabel = StatisticsEvents.PrivilegeLevel.GOLD;
                break;
            case PLATINUM:
                eventLabel = StatisticsEvents.PrivilegeLevel.PLATINUM;
                break;
            case DIAMOND:
                eventLabel = StatisticsEvents.PrivilegeLevel.DIAMOND;
                break;
            case BLACK_GOLD:
                eventLabel = StatisticsEvents.PrivilegeLevel.BLACK_GOLD;
                break;
            default:
                break;
        }
        return eventLabel;
    }

    //是不是更多权限
    private boolean isMorePrivilege(MemberPrivilege memberPrivilege, int position) {
        boolean isLastOne = (position == mPrivilegeAdapter.getItemCount() - 1);
        return isLastOne;
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mAccountInfo = (AccountInfo) intent.getSerializableExtra("accountInfo");
        if (mAccountInfo == null) {
            LogUtil.i(TAG, "initData() mAccountInfo == null");
            return;
        }

        updateAccountView(mAccountInfo);
    }

    @Override
    protected int getLayoutResId() {
        return ResourceUtil.getLayoutId(this, "uc_activity_member_privilege");
    }

    @Override
    protected String getActionbarTitle() {
        return getString(ResourceUtil.getStringId(this, "uc_title_member_privilege_actionbar"));
    }


    @Override
    public void showMemberPrivilegesLoadingView() {
        mPrivilegeLoadingView.setVisibility(View.VISIBLE);
        mRecyclePrivileges.setVisibility(View.GONE);
    }

    @Override
    public void showMemberPrivileges(List<MemberPrivilege> memberPrivilegeList) {
        mPrivilegeLoadingView.setVisibility(View.GONE);
        mRecyclePrivileges.setVisibility(View.VISIBLE);
        if (memberPrivilegeList == null || memberPrivilegeList.isEmpty()) {
            LogUtil.d("memberPrivilegeList is null or empty");
            return;
        }
        LogUtil.d("memberPrivilegeList size=" + memberPrivilegeList.size());
        mPrivilegeList = memberPrivilegeList;
        mPrivilegeAdapter.setDatas(mPrivilegeList);
    }

    @Override
    public void showMemberPrivilegesLoadFailView(Throwable throwable) {
        mPrivilegeLoadingView.setVisibility(View.GONE);
    }

    AccountInfo mAccountInfo = null;
    @Override
    public void showAccountView(AccountInfo accountInfo) {
        if (accountInfo == null) {
            if (mAccountInfo == null) {
                ToastUtil.showShort(this, ResourceUtil.getString(this, "uc_txt_member_privilege_get_account_info_fail"));
                mPrivilegeLoadingView.setVisibility(View.GONE);
                mRecyclePrivileges.setVisibility(View.VISIBLE);
                return;
            } else {
                LogUtil.i(TAG, "use AccountInfo from intent, level = " + mAccountInfo.getMemberLevel());
                mPrivilegeLoadingView.setVisibility(View.GONE);
                mRecyclePrivileges.setVisibility(View.VISIBLE);
                updateAccountView(mAccountInfo);
                return;
            }
        }

        if (accountInfo.isSameMemberLevel(mAccountInfo)) {
            mPrivilegeLoadingView.setVisibility(View.GONE);
            mRecyclePrivileges.setVisibility(View.VISIBLE);
            return;
        }
        mAccountInfo = accountInfo;
        updateAccountView(accountInfo);
    }

    private void updateAccountView(AccountInfo accountInfo) {
        MemberLevel memberLevel = accountInfo.getMemberLevel();
        if (memberLevel == null) {
            LogUtil.i(TAG, "updateAccountView() memberLevel == null");
            memberLevel = MemberLevel.GOLD;
        }

        if (mPresenter != null) {
            if (getInitIndex(memberLevel) != mUserLevelPrivilege.getCurrentAccountIndex()) {
                mPrivilegeLoadingView.setVisibility(View.VISIBLE);
                mRecyclePrivileges.setVisibility(View.GONE);
                mPresenter.loadMemberPrivileges(memberLevel);
            } else {
                mPrivilegeLoadingView.setVisibility(View.GONE);
                mRecyclePrivileges.setVisibility(View.VISIBLE);
            }
        }
        if (accountInfo.getPhoto() != null) {
            mImgPhoto.setImageBitmap(accountInfo.getPhoto());
        } else {
            mImgPhoto.setBackgroundResource(getDrawableId(this, "uc_bg_member_card_user_photo_default"));
        }

        switch (memberLevel) {

            case PLATINUM:
                mTxtUserLevel.setText(ResourceUtil.getString(this, "uc_txt_member_privilege_user_level_platinum"));
                break;
            case DIAMOND:
                mTxtUserLevel.setText(ResourceUtil.getString(this, "uc_txt_member_privilege_user_level_diamond"));
                break;
            case BLACK_GOLD:
                mTxtUserLevel.setText(ResourceUtil.getString(this, "uc_txt_member_privilege_user_level_black_gold"));
                break;
            case NORMAL:
            case GOLD:
            default:
                mTxtUserLevel.setText(ResourceUtil.getString(this, "uc_txt_member_privilege_user_level_gold"));
                break;
        }

        int growthValue = memberLevel.getGrowthValue();

        List<MemberLevelInfo> memberLevelInfos = memberLevel.getMemberLevelInfos();
        mUserLevelPrivilege.setLevelInfoList(memberLevelInfo2LevelInfo(memberLevelInfos));
        mUserLevelPrivilege.setProgress(growthValue, getMaxLevelValue(memberLevelInfos));
        mUserLevelPrivilege.setSelectedIndex(getInitIndex(memberLevel));

        int max = getMaxLevelValue(memberLevelInfos);
        mProgressExperience.setMax(max);
        mProgressExperience.setProgress(growthValue);
        if (growthValue >= max) {
            mTxtProgress.setText(String.valueOf(growthValue));
        } else {
            mTxtProgress.setText(growthValue + "/" + max);
        }
        mUserLevelPrivilege.setOnLevelClickListener(new UserLevelView.OnLevelClickListener() {
            @Override
            public void onLevelItemClick(int index) {
                showMemberPrivilegesLoadingView();
                LogUtil.d(TAG, "onLevelItemClick index=" + index);
                String eventId = "";
                if (index == 0) {
                    mPresenter.loadMemberPrivileges(MemberLevel.GOLD);
                    eventId = StatisticsEvents.PrivilegeLevel.GOLD;
                } else if (index == 1) {
                    mPresenter.loadMemberPrivileges(MemberLevel.PLATINUM);
                    eventId = StatisticsEvents.PrivilegeLevel.PLATINUM;
                } else if (index == 2) {
                    mPresenter.loadMemberPrivileges(MemberLevel.DIAMOND);
                    eventId = StatisticsEvents.PrivilegeLevel.DIAMOND;
                } else {
                    mPresenter.loadMemberPrivileges(MemberLevel.BLACK_GOLD);
                    eventId = StatisticsEvents.PrivilegeLevel.BLACK_GOLD;
                }
                StatisticsUtil.onEvent(MemberPrivilegeActivity.this, eventId);
            }
        });
    }

    private int getInitIndex(MemberLevel level) {
        switch (level) {
            case PLATINUM:
                return 1;
            case DIAMOND:
                return 2;
            case BLACK_GOLD:
                return 3;
            case NORMAL:
            default:
                return 0;
        }
    }

    private int getMaxLevelValue(List<MemberLevelInfo> memberLevelInfos) {
        if (memberLevelInfos == null) {
            return 1000;
        }
        int max = 0;
        for (MemberLevelInfo info : memberLevelInfos) {
            if (info.getRank() == MemberLevel.BLACK_GOLD.getValue()) {
                max = info.getMinValue();
                break;
            }
        }
        return max;
    }

    private List<UserLevelView.LevelInfo> initLevelInfo() {
        List<UserLevelView.LevelInfo> levelInfos = new ArrayList<UserLevelView.LevelInfo>();

        UserLevelView.LevelInfo gold = new UserLevelView.LevelInfo();
        gold.name = ResourceUtil.getString(this, "uc_txt_member_privilege_user_level_gold");
        gold.icon = ResourceUtil.getDrawableId(this, "uc_ic_member_privilege_gold_normal");
        gold.selectIcon = ResourceUtil.getDrawableId(this, "uc_ic_member_privilege_gold_selected");

        UserLevelView.LevelInfo platinum = new UserLevelView.LevelInfo();
        platinum.name = ResourceUtil.getString(this, "uc_txt_member_privilege_user_level_platinum");
        platinum.icon = ResourceUtil.getDrawableId(this, "uc_ic_member_privilege_platinum_normal");
        platinum.selectIcon = ResourceUtil.getDrawableId(this, "uc_ic_member_privilege_platinum_selected");

        UserLevelView.LevelInfo diamond = new UserLevelView.LevelInfo();
        diamond.name = ResourceUtil.getString(this, "uc_txt_member_privilege_user_level_diamond");
        diamond.icon = ResourceUtil.getDrawableId(this, "uc_ic_member_privilege_diamond_normal");
        diamond.selectIcon = ResourceUtil.getDrawableId(this, "uc_ic_member_privilege_diamond_selected");


        UserLevelView.LevelInfo blackGold = new UserLevelView.LevelInfo();
        blackGold.name = ResourceUtil.getString(this, "uc_txt_member_privilege_user_level_black_gold");
        blackGold.icon = ResourceUtil.getDrawableId(this, "uc_ic_member_privilege_black_gold_normal");
        blackGold.selectIcon = ResourceUtil.getDrawableId(this, "uc_ic_member_privilege_black_gold_selected");
        levelInfos.add(gold);
        levelInfos.add(platinum);
        levelInfos.add(diamond);
        levelInfos.add(blackGold);
        return levelInfos;
    }

    private List<UserLevelView.LevelInfo> memberLevelInfo2LevelInfo(List<MemberLevelInfo> memberLevelInfos) {

        List<UserLevelView.LevelInfo> levelInfos = initLevelInfo();
        int N = 0;
        if (memberLevelInfos != null) {
            N = memberLevelInfos.size();
        }
        if (N != 4) {
            return levelInfos;
        }
        for (int i = 0; i < N; i++) {
            UserLevelView.LevelInfo levelInfo = levelInfos.get(i);
            MemberLevelInfo info = memberLevelInfos.get(i);

            levelInfo.minValue = info.getMinValue();
            levelInfo.maxValue = info.getMaxValue();
            String experience = ResourceUtil.getString(this, "uc_txt_member_privilege_experience");
            if (i == 0) {
                levelInfo.number = experience + levelInfo.minValue;

            } else {
                levelInfo.number = experience + levelInfo.minValue + "+";
            }
        }
        return levelInfos;
    }

    private static class RecyclePrivilegeAdapter extends BaseRecyclerViewAdapter<MemberPrivilege> {
        private IAppContext mAppContext;

        public RecyclePrivilegeAdapter(IAppContext appContext, List<MemberPrivilege> dataList) {
            super(appContext.application(), dataList);
            mAppContext = PreconditionsUtil.checkNotNull(appContext);
        }

        @Override
        public void convert(ViewHolder holder, MemberPrivilege memberPrivilege, int position) {
            Context context = mAppContext.application();
            TextView txtName = holder.getView(getWidgetId(context, "txt_list_item_member_privilege"));
            txtName.setText(memberPrivilege.getName());
            ImageView imgIcon = holder.getView(getWidgetId(context, "img_list_item_member_privilege"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imgIcon.setBackgroundResource(ResourceUtil.getDrawableId(context, "uc_member_privilege_list_item_ripple"));
            }
            if (!TextUtils.isEmpty(memberPrivilege.getIconUrl())) {
                ImageConfig.Builder builder = new ImageConfig.Builder();
                builder.setUrl(memberPrivilege.getIconUrl())
                        .setImageView(imgIcon)
                        .setPlaceholder(ResourceUtil.getDrawableId(context, "uc_ic_member_privilege_list_item_icon_default"))
                        .setErrorPic(ResourceUtil.getDrawableId(context, "uc_ic_member_privilege_list_item_icon_default"))
                        .setCacheInDisk(true)
                        .setCacheInMemory(true);
                ImageLoaderImpl.create(context).loadImage(builder.build());
            } else {
                imgIcon.setImageResource(ResourceUtil.getDrawableId(context, "uc_ic_member_privilege_list_item_icon_default"));
            }
        }

        @Override
        public int getLayoutId() {
            return ResourceUtil.getLayoutId(mAppContext.application(), "uc_list_item_member_privilege");
        }
    }

}
