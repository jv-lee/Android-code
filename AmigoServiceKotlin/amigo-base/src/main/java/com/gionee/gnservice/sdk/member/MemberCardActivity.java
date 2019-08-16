package com.gionee.gnservice.sdk.member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gionee.account.sdk.itf.GioneeAccount;
import com.gionee.account.sdk.itf.utils.Function;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.entity.AccountInfo;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.sdk.coupon.MemberCouponActivity;
import com.gionee.gnservice.sdk.integral.MemberIntegralActivity;
import com.gionee.gnservice.sdk.member.mvp.IMemberCardContract;
import com.gionee.gnservice.sdk.member.mvp.MemberCardPresenter;
import com.gionee.gnservice.statistics.StatisticsEvents;
import com.gionee.gnservice.statistics.StatisticsUtil;
import com.gionee.gnservice.utils.ChameleonColorManager;
import com.gionee.gnservice.utils.ImageOOMUtil;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PhoneUtil;
import com.gionee.gnservice.utils.RepeatClickUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.SdkUtil;
import com.gionee.gnservice.widget.RoundImageView;
import com.gionee.gnservice.widget.fresh.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import static com.gionee.gnservice.utils.ResourceUtil.getWidgetId;

/**
 * Created by caocong on 1/6/17.
 */
public class MemberCardActivity extends BaseActionbarTransparentActivity implements IMemberCardContract.View, View.OnClickListener {
    private static final String TAG = MemberCardActivity.class.getSimpleName();
    private static final int ID_ITEM_INTEGRAL = 0;
    private static final int ID_ITEM_WALLET = 1;
    private static final int ID_ITEM_PRIVILEGE = 2;
    private static final int ID_ITEM_COUPON = 3;
    private static final int ID_ITEM_SETTING = 4;

    private RoundImageView mImgPhoto;
    private ImageView mImgMemberLevelMark;
    private TextView mTxtUserName;
    private ListView mListMenu1, mListMenu2, mListMenu3;
    private List<ItemInfo> mItemInfoList1, mItemInfoList2, mItemInfoList3;
    private ItemAdapter mItemAdapter1, mItemAdapter2, mItemAdapter3;
    private View mLayoutMemberCard;
    private IMemberCardContract.Presenter mPresenter;
    private ItemInfo mItemInfo1, mItemInfo2, mItemInfo3, mItemInfo4, mItemInfo5;
    private Bitmap mPhoto;
    private AccountInfo mAccountInfo;
    private String mIntegralNumber;
    private TwinklingRefreshLayout mRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        boolean isFromSdk = "com.gionee.gnservice.sdk.toMemberCardActivity".equals(intent.getAction());
        if (isFromSdk) {
            SdkUtil.setFromSdkDemo(true);
        } else {
            SdkUtil.setFromSdkDemo(false);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void addChameleonColorView() {
        getView(getWidgetId(this, "layout_member_menu")).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
        getView(getWidgetId(this, "layout_member_card_menu_1")).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
        getView(getWidgetId(this, "layout_member_card_menu_2")).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
        getView(getWidgetId(this, "layout_member_card_menu_3")).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
    }

    @Override
    protected void initVariables() {
        Intent intent = getIntent();

        mPresenter = new MemberCardPresenter(appContext(), this);
        mItemInfoList1 = new ArrayList<ItemInfo>();
        mItemInfoList2 = new ArrayList<ItemInfo>();
        mItemInfoList3 = new ArrayList<ItemInfo>();
        mItemInfo1 = new ItemInfo(ID_ITEM_INTEGRAL, ResourceUtil.getDrawableId(this, "uc_ic_member_card_integral"), ResourceUtil.getStringId(this, "uc_txt_member_card_integral"));
        mIntegralNumber = intent.getStringExtra("integral");
        mItemInfo1.value = String.format(ResourceUtil.getString(this, "uc_txt_member_card_integral_value"), mIntegralNumber);

        mItemInfo2 = new ItemInfo(ID_ITEM_PRIVILEGE, ResourceUtil.getDrawableId(this, "uc_ic_member_card_privilege"), ResourceUtil.getStringId(this, "uc_txt_member_card_privilege"));
        mItemInfo2.value = String.format(ResourceUtil.getString(this, "uc_txt_member_card_privilege_value"), intent.getStringExtra("privilegeSize"));

        mItemInfo3 = new ItemInfo(ID_ITEM_WALLET, ResourceUtil.getDrawableId(this, "uc_ic_member_card_wallet"), ResourceUtil.getStringId(this, "uc_txt_member_card_wallet"));
        mItemInfo3.value = String.format(ResourceUtil.getString(this, "uc_txt_member_card_wallet_value"), intent.getStringExtra("acoin"));


        mItemInfo4 = new ItemInfo(ID_ITEM_COUPON, ResourceUtil.getDrawableId(this, "uc_ic_member_card_coupon"), ResourceUtil.getStringId(this, "uc_txt_member_card_coupon"));
        mItemInfo4.value = String.format(ResourceUtil.getString(this, "uc_txt_member_card_coupon_value"), (intent.getStringExtra("coupon")));

        mItemInfo5 = new ItemInfo(ID_ITEM_SETTING, ResourceUtil.getDrawableId(this, "uc_ic_member_card_setting"), ResourceUtil.getStringId(this, "uc_txt_member_card_setting"));
        //hclw不显示积分
        //mItemInfoList1.add(mItemInfo1);
        if (showPrivilege()) {
            mItemInfoList1.add(mItemInfo2);
        }

        if (showWallet()) {
            mItemInfoList2.add(mItemInfo3);
        }

        mItemInfoList2.add(mItemInfo4);

        if (showSetting()) {
            mItemInfoList3.add(mItemInfo5);
        }

    }

    @Override
    protected void initView() {
        setMemberCardBackground();
        mRefreshLayout = getView(ResourceUtil.getWidgetId(this, "refresh_member_card"));
        mRefreshLayout.setPureScrollModeOn();
        mImgPhoto = getView(ResourceUtil.getWidgetId(this, "img_member_card_user_photo"));
        mImgPhoto.setBackgroundResource(ResourceUtil.getDrawableId(this, "uc_bg_member_card_user_photo_default"));
        mTxtUserName = getView(ResourceUtil.getWidgetId(this, "txt_member_card_user_name"));
        mImgMemberLevelMark = getView(ResourceUtil.getWidgetId(this, "img_member_card_user_level_mark"));
        mLayoutMemberCard = getView(ResourceUtil.getWidgetId(this, "llayout_member_card_user"));
        mLayoutMemberCard.setOnClickListener(this);

        mListMenu1 = getView(ResourceUtil.getWidgetId(this, "list_member_card_menu_1"));
        mListMenu2 = getView(ResourceUtil.getWidgetId(this, "list_member_card_menu_2"));
        mListMenu3 = getView(ResourceUtil.getWidgetId(this, "list_member_card_menu_3"));

        mItemAdapter1 = new ItemAdapter(this, mItemInfoList1);
        mItemAdapter2 = new ItemAdapter(this, mItemInfoList2);
        mItemAdapter3 = new ItemAdapter(this, mItemInfoList3);

        mListMenu1.setOnItemClickListener(new CustomerOnItemClickListener(this, mItemAdapter1));
        mListMenu2.setOnItemClickListener(new CustomerOnItemClickListener(this, mItemAdapter2));
        mListMenu3.setOnItemClickListener(new CustomerOnItemClickListener(this, mItemAdapter3));

        mListMenu1.setAdapter(mItemAdapter1);
        mListMenu2.setAdapter(mItemAdapter2);
        mListMenu3.setAdapter(mItemAdapter3);


        if (mItemInfoList1.isEmpty()) {
            getView(getWidgetId(this, "layout_member_card_menu_1")).setVisibility(View.GONE);
        }
        if (mItemInfoList2.isEmpty()) {
            getView(getWidgetId(this, "layout_member_card_menu_2")).setVisibility(View.GONE);
        }
        if (mItemInfoList3.isEmpty()) {
            getView(getWidgetId(this, "layout_member_card_menu_3")).setVisibility(View.GONE);
        }

        if (!showUserInfo()) {
            getView(getWidgetId(this, "img_member_card_user_info_goto")).setVisibility(View.GONE);
            getView(getWidgetId(this, "txt_member_card_see_user_info")).setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        AccountInfo accountInfo = (AccountInfo) intent.getSerializableExtra("accountInfo");
        if (accountInfo != null) {
            showAccountInfoView(accountInfo);
        }
    }

    // 为账号sdk6.0.1.m兼容外放版查看个人资料
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d(TAG, "onActivityResult, resultCode: " + resultCode);
        GioneeAccount.getInstance(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2 || resultCode == -1) {
            LogUtil.d(TAG, "finish this activity.");
            finish();
        }
    }

    private void setMemberCardBackground() {
        final Context context = getApplicationContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = ImageOOMUtil.getBitmap(context, ResourceUtil.getDrawableId(context, "uc_bg_member_privilege_actionbar"));
                if (bitmap == null) {
                    return;
                }

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        View card = getView(getWidgetId(context, "layout_member_card"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            card.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                        }
                    }
                });
            }
        }).start();
    }

    public Bitmap readBitmap(Context context, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    private boolean showSetting() {
        return !SdkUtil.isCallBySdk(this);
    }

    private boolean showWallet() {
        Intent intent = new Intent("com.gionee.gsp.wallet.components.activities.WalletMainActivity");
        intent.putExtra("app_id", AppConfig.Account.getAmigoServiceAppId());
        return intent.resolveActivity(getPackageManager()) != null;
    }

    private boolean showUserInfo() {
        GioneeAccount gioneeAccount = GioneeAccount.getInstance(this);
        return gioneeAccount.isFunctionExist(this, Function.PERSONAL_ACTIVITY);
    }

    private boolean showPrivilege() {
        /*GioneeAccount gioneeAccount = GioneeAccount.getInstance(this);
        return gioneeAccount.isFunctionExist(this, Function.GET_USER_RANK);*/
        return true;
    }

    @Override
    public void onClick(View view) {
        if (!RepeatClickUtil.canRepeatClick(view)) {
            return;
        }
        if (view.getId() == ResourceUtil.getWidgetId(this, "llayout_member_card_user")) {
            startMemberInfoActivity();
        }
    }


    private static class CustomerOnItemClickListener implements AdapterView.OnItemClickListener {

        private ItemAdapter mAdapter;
        private MemberCardActivity mActivity;

        public CustomerOnItemClickListener(MemberCardActivity activity, ItemAdapter adapter) {
            mAdapter = adapter;
            mActivity = activity;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long cid) {
            if (!RepeatClickUtil.canRepeatClick(view)) {
                return;
            }
            int id = mAdapter.getItem(position).id;
            if (id == ID_ITEM_INTEGRAL) {
                mActivity.startMemberIntegralActivity();
            } else if (id == ID_ITEM_WALLET) {
                mActivity.startMemberWalletActivity();
            } else if (id == ID_ITEM_PRIVILEGE) {
                mActivity.startMemberPrivilegeActivity();
            } else if (id == ID_ITEM_COUPON) {
                mActivity.startMemberCouponActivity();
            } else if (id == ID_ITEM_SETTING) {
                mActivity.startMemberSettingActivity();
            }
        }
    }

    private void startMemberCouponActivity() {
        Intent intent = new Intent(this, MemberCouponActivity.class);
        startActivity(intent);
        StatisticsUtil.onEvent(this, StatisticsEvents.Main.COUPON);
    }

    private void startMemberIntegralActivity() {
        Intent intent = new Intent(this, MemberIntegralActivity.class);
        intent.putExtra("integral", mIntegralNumber);
        startActivity(intent);
        StatisticsUtil.onEvent(this, StatisticsEvents.Main.MY_SCORE);
    }

    private void startMemberInfoActivity() {
        try {
            if (showUserInfo()) {
                appContext().accountHelper().toPersonalInfo(this);
                StatisticsUtil.onEvent(this, StatisticsEvents.Main.PERSONAL_INFO);
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(ResourceUtil.getStringId(this, "uc_gionee_account_not_ready")), Toast.LENGTH_LONG).show();
        }
    }

    private void startMemberPrivilegeActivity() {
        Intent intent = new Intent(this, MemberPrivilegeActivity.class);
        intent.putExtra("accountInfo", mAccountInfo);
        intent.putExtra("photo", mPhoto);
        startActivity(intent);
        StatisticsUtil.onEvent(this, StatisticsEvents.Main.PRIVILEGE);
    }

    private void startMemberSettingActivity() {
        try {
            Intent intent = new Intent(this, Class.forName("com.gionee.gnservice.module.setting.MemberSettingActivity"));
            startActivity(intent);
            StatisticsUtil.onEvent(this, StatisticsEvents.Settings.SETTINGS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startMemberWalletActivity() {
        Intent intent = new Intent("com.gionee.gsp.wallet.components.activities.WalletMainActivity");
        intent.putExtra("app_id", AppConfig.Account.AMIGO_SERVICE_APP_ID);
        startActivity(intent);
        StatisticsUtil.onEvent(this, StatisticsEvents.Main.GIONEE_WALLET);
    }

    @Override
    protected int getLayoutResId() {
        return ResourceUtil.getLayoutId(this, "uc_activity_member_card");
    }

    @Override
    protected String getActionbarTitle() {
        return getString(ResourceUtil.getStringId(this, "uc_title_member_card_actionbar"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.loadAccountInfo();
        }
        if (!showWallet() && mItemInfoList2.contains(mItemInfo3)) {
            mItemInfoList2.remove(mItemInfo3);
            mItemAdapter2.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showAccountView(AccountInfo accountInfo) {
        mAccountInfo = accountInfo;
        showAccountInfoView(accountInfo);
        LogUtil.d(TAG, "get accountinfo is:" + accountInfo.toString());

    }

    @Override
    public void showPhoto(Bitmap photo) {
        LogUtil.d(TAG, "photo is null :" + (photo == null));
        if (photo != null) {
            mPhoto = photo;
            mImgPhoto.setImageBitmap(photo);
        }
    }

    @Override
    public void showMemerLevelView(MemberLevel level) {
        if (level == null) {
            LogUtil.i(TAG, "level param is null, return");
            return;
        }
        showMemberLevelView(level);
        if (mAccountInfo == null) {
            LogUtil.i(TAG, "mAccountInfo is null, maybe showAccoutView is not called.");
            mAccountInfo = new AccountInfo();
        }
        mAccountInfo.setMemberLevel(level);
    }

    private void showAccountInfoView(AccountInfo info) {
        if (!TextUtils.isEmpty(info.getNickName())) {
            mTxtUserName.setText(info.getNickName());
            return;
        }
        if (!TextUtils.isEmpty(info.getUserName())) {
            mTxtUserName.setText(PhoneUtil.formatPhoneNumber(info.getUserName()));
        }

    }

    private void showMemberLevelView(MemberLevel memberLevel) {
        switch (memberLevel) {
            case NORMAL:
            case GOLD:
                mImgMemberLevelMark.setBackgroundResource(ResourceUtil.getDrawableId(this, "uc_ic_member_card_mark_gold"));
                break;
            case DIAMOND:
                mImgMemberLevelMark.setBackgroundResource(ResourceUtil.getDrawableId(this, "uc_ic_member_card_mark_diamond"));
                break;
            case PLATINUM:
                mImgMemberLevelMark.setBackgroundResource(ResourceUtil.getDrawableId(this, "uc_ic_member_card_mark_platinum"));
                break;
            case BLACK_GOLD:
                mImgMemberLevelMark.setBackgroundResource(ResourceUtil.getDrawableId(this, "uc_ic_member_card_mark_black_gold"));
                break;
            default:
                break;
        }
    }

    @Override
    public void showACoinBalance(String coin) {
        LogUtil.d(TAG, "show a coin is:" + coin);
        mItemInfo3.value = String.format(ResourceUtil.getString(this, "uc_txt_member_card_wallet_value"), coin);
        mItemAdapter1.notifyDataSetChanged();
    }

    @Override
    public void showUserIntegral(int value) {
        //hclw不显示积分
        //LogUtil.i(TAG, "showUserIntegral, value = " + value);
        //mIntegralNumber = String.valueOf(value);
        //mItemInfo1.value = String.format(ResourceUtil.getString(this, "uc_txt_member_card_integral_value"), mIntegralNumber);
        //mItemAdapter1.notifyDataSetChanged();
    }

    @Override
    public void showPrivilegesSize(int size) {
        LogUtil.d(TAG, "show privilege size is:" + size);
        mItemInfo2.value = String.format(ResourceUtil.getString(this, "uc_txt_member_card_privilege_value"), String.valueOf(size));
        mItemAdapter2.notifyDataSetChanged();
    }

    @Override
    public void showCouponNumber(int value) {
        mItemInfo4.value = String.format(ResourceUtil.getString(this, "uc_txt_member_card_coupon_value"), String.valueOf(value));
        mItemAdapter2.notifyDataSetChanged();
    }

    private static class ItemAdapter extends BaseAdapter {
        private List<ItemInfo> mItemInfos;
        private Context mContext;

        public ItemAdapter(Context context, List<ItemInfo> itemInfos) {
            this.mContext = context;
            setData(itemInfos);
        }

        public void setData(List<ItemInfo> itemInfos) {
            if (itemInfos != null) {
                mItemInfos = itemInfos;
                notifyDataSetChanged();
            } else {
                mItemInfos = new ArrayList<ItemInfo>();
            }
        }

        @Override
        public int getCount() {
            return mItemInfos.size();
        }

        @Override
        public ItemInfo getItem(int position) {
            return mItemInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(ResourceUtil.getLayoutId(mContext, "uc_list_item_member_card"), null);
                viewHolder = new ViewHolder();
                viewHolder.imgIcon = getView(convertView, ResourceUtil.getWidgetId(mContext, "img_list_item_member_card"));
                viewHolder.txtName = getView(convertView, ResourceUtil.getWidgetId(mContext, "txt_list_item_member_card_name"));
                viewHolder.txtValue = getView(convertView, ResourceUtil.getWidgetId(mContext, "txt_list_item_member_card_info"));
                viewHolder.viewLine = getView(convertView, ResourceUtil.getWidgetId(mContext, "view_list_item_member_card_line"));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ItemInfo itemInfo = getItem(position);
            viewHolder.imgIcon.setImageResource(itemInfo.iconResId);
            viewHolder.txtName.setText(mContext.getString(itemInfo.nameResId));
            String value = itemInfo.value;
            if (!TextUtils.isEmpty(value)) {
                viewHolder.txtValue.setText(value);
            }

            if (position == getCount() - 1) {
                viewHolder.viewLine.setVisibility(View.GONE);
            } else {
                viewHolder.viewLine.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        <T extends View> T getView(View view, int id) {
            return (T) view.findViewById(id);
        }

        private static class ViewHolder {
            ImageView imgIcon;
            TextView txtName;
            TextView txtValue;
            View viewLine;
        }
    }

    private static class ItemInfo {
        int id;
        int iconResId;
        int nameResId;
        String value;

        public ItemInfo(int id, int iconResId, int nameResId) {
            this.id = id;
            this.iconResId = iconResId;
            this.nameResId = nameResId;
        }
    }

}
