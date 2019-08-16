package com.gionee.gnservice.sdk.integral;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import com.gionee.gnservice.base.BaseSdkActivity;
import com.gionee.gnservice.sdk.integral.mvp.IMemberIntegralContract;
import com.gionee.gnservice.sdk.integral.mvp.MemberIntegralPresenter;
import com.gionee.gnservice.statistics.StatisticsEvents;
import com.gionee.gnservice.statistics.StatisticsUtil;
import com.gionee.gnservice.utils.ChameleonColorManager;
import com.gionee.gnservice.utils.ImageOOMUtil;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.PhoneUtil;
import com.gionee.gnservice.utils.RepeatClickUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.SdkUtil;
import com.gionee.gnservice.widget.AutoVerticalScrollTextView;

import java.util.ArrayList;
import java.util.List;

import static com.gionee.gnservice.utils.ResourceUtil.getDrawableId;
import static com.gionee.gnservice.utils.ResourceUtil.getLayoutId;
import static com.gionee.gnservice.utils.ResourceUtil.getStringId;
import static com.gionee.gnservice.utils.ResourceUtil.getWidgetId;

/**
 * Created by caocong on 1/9/17.
 */
public class MemberIntegralActivity extends BaseSdkActivity implements IMemberIntegralContract.View {
    private static final String TAG = MemberIntegralActivity.class.getSimpleName();
    private static final int ID_ITEM_LOTTERY = 0;
    private static final int ID_ITEM_MALL = 1;
    private static final int ID_ITEM_MAKE = 2;
    private ListView mListMenu;
    private List<ItemInfo> mItemInfoList;
    private ItemAdapter mAdapter;
    private IMemberIntegralContract.Presenter mPresenter;
    private TextView mTxtIntegralNum;
    private View mBtnIntegralRule;
    private View mLayoutPrize;
    private AutoVerticalScrollTextView mTxtPrize;
    private TextUpDownScroll mPrizeScroll;
    private ItemInfo mItemInfo1, mItemInfo2, mItemInfo3;
    private int mUserIntegral;


    @Override
    protected void initVariables() {
        mPresenter = new MemberIntegralPresenter(appContext(), this);
        mItemInfoList = new ArrayList<ItemInfo>();
        mItemInfo1 = new ItemInfo(ID_ITEM_LOTTERY, getDrawableId(this, "uc_ic_member_integral_daily_lottery"), getStringId(this, "uc_txt_member_integral_lottery"));
        mItemInfo2 = new ItemInfo(ID_ITEM_MALL, getDrawableId(this, "uc_ic_member_integral_mall"), getStringId(this, "uc_txt_member_integral_mall"));
        mItemInfo3 = new ItemInfo(ID_ITEM_MAKE, getDrawableId(this, "uc_ic_member_integral_make"), getStringId(this, "uc_txt_member_integral_make"));

        mItemInfoList.add(mItemInfo1);
        mItemInfoList.add(mItemInfo2);
        if (showIntegralMake()) {
            mItemInfoList.add(mItemInfo3);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.loadIntegralPrizes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadIntegralUser(null);
    }

    private boolean showIntegralMake() {
        return !SdkUtil.isCallBySdk(this);
    }


    @Override
    protected void initActionBar() {
        super.initActionBar();
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String integralNumber = intent.getStringExtra("integral");
        if (TextUtils.isEmpty(integralNumber)) {
            integralNumber = "0";
        }
        setTopBackground();
        mUserIntegral = Integer.parseInt(integralNumber);
        mItemInfo2.description = String.format(ResourceUtil.getString(this, "uc_txt_member_integral_mall_desription"), Integer.parseInt(integralNumber));
        mListMenu = getView(getWidgetId(this, "listview_member_integral_menu"));
        mAdapter = new ItemAdapter(this);
        mAdapter.setData(mItemInfoList);
        mListMenu.setAdapter(mAdapter);
        mListMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long cid) {
                if (!RepeatClickUtil.canRepeatClick(view)) {
                    return;
                }
                if (PhoneUtil.isRunningByMonkey()) {
                    return;
                }

                int id = mAdapter.getItem(position).id;
                Intent intent = null;
                String eventId = "";
                if (id == ID_ITEM_LOTTERY) {
                    intent = new Intent(MemberIntegralActivity.this, MemberIntegralLotteryActivity.class);
                    eventId = StatisticsEvents.Main.LOTTERY;
                } else if (id == ID_ITEM_MALL) {
                    intent = new Intent(MemberIntegralActivity.this, MemberIntegralMallActivity.class);
                    eventId = StatisticsEvents.Main.SCORE_MALL;
                } else if (id == ID_ITEM_MAKE) {
                    intent = new Intent();
                    intent.setClassName(MemberIntegralActivity.this, "com.gionee.gnservice.module.integral.MemberIntegralMakeActivity");
                    //intent = new Intent(MemberIntegralActivity.this, MemberIntegralMakeActivity.class);
                    eventId = StatisticsEvents.Main.GAIN_SCORE;
                }
                if (intent != null) {
                    startActivity(intent);
                    StatisticsUtil.onEvent(MemberIntegralActivity.this, eventId, StatisticsEvents.Label.FROM_MY_SCORE);
                }
            }
        });
        mTxtIntegralNum = getView(getWidgetId(this, "txt_member_integral_num"));
        mTxtIntegralNum.setText(integralNumber);

        mBtnIntegralRule = getView(getWidgetId(this, "btn_member_integral_rule"));
        mBtnIntegralRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntegralRuleActivity();
            }
        });
        mLayoutPrize = getView(getWidgetId(this, "layout_member_integral_prize"));
        mTxtPrize = getView(getWidgetId(this, "txt_member_integral_prize"));
    }

    private void setTopBackground() {
        final Context context = getApplicationContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = ImageOOMUtil.getBitmap(context, ResourceUtil.getDrawableId(context, "uc_bg_member_integral_top"));
                if (bitmap == null) {
                    return;
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        View topBackground = getView(getWidgetId(context, "rlayout_member_integral"));
                        topBackground.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                    }
                });
            }
        }).start();
    }

    protected void addChameleonColorView() {
        getView(getWidgetId(this, "layout_member_integral")).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
        getView(getWidgetId(this, "listview_member_integral_menu")).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
        mLayoutPrize.setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
    }

    private void startIntegralRuleActivity() {
        Intent intent = new Intent(this, MemberIntegralRuleActivity.class);
        startActivity(intent);
        StatisticsUtil.onEvent(this, StatisticsEvents.Main.SCORE_RULE);
    }

    @Override
    protected int getLayoutResId() {
        return getLayoutId(this, "uc_activity_member_integral");
    }

    @Override
    protected String getActionbarRightTitle() {
        return getResources().getString(getStringId(this, "uc_title_member_integral_record_actionbar"));
    }

    @Override
    protected void onActionRightTextClick(View view) {
        Intent intent = new Intent(this, MemberIntegralRecordActivity.class);
        intent.putExtra("type", MemberIntegralRecordActivity.TYPE_INTEGRAL_RECORD);
        intent.putExtra("user_integral", mUserIntegral);
        startActivity(intent);
        StatisticsUtil.onEvent(this, StatisticsEvents.Main.SCORE_RECORD);
    }

    @Override
    protected String getActionbarTitle() {
        return getResources().getString(getStringId(this, "uc_title_member_integral_actionbar"));
    }

    @Override
    public void showIntegralPrizeView(List<String> prizeList) {
        if (prizeList != null && !prizeList.isEmpty()) {
            mLayoutPrize.setVisibility(View.VISIBLE);
            mPrizeScroll = new TextUpDownScroll(mTxtPrize, prizeList);
            mPrizeScroll.startScroll();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPrizeScroll != null) {
            mPrizeScroll.endScroll();
        }
    }

    @Override
    public void showIntegralUserView(int value) {
        mPresenter.loadLotteryRemainTimes(null);
        LogUtil.d(TAG, "showIntegralUser is:" + value + ";thread is:" + Thread.currentThread().getName());
        mTxtIntegralNum.setText(String.valueOf(value));
        mItemInfo2.description = String.format(ResourceUtil.getString(this, "uc_txt_member_integral_mall_desription"), value);
        mAdapter.notifyDataSetChanged();
        mUserIntegral = value;
    }

    @Override
    public void showLotteryRemainTimeView(int times) {
        mPresenter.loadIntegralMakeTasksNumber(null);
        LogUtil.d(TAG, "showLotteryRemainTimeView is:" + times);
        if (times == 0) {
            mItemInfo1.description = ResourceUtil.getString(this, "uc_txt_member_integral_lottery_desription_no_time");
        } else {
            mItemInfo1.description = String.format(ResourceUtil.getString(this, "uc_txt_member_integral_lottery_desription"), times);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showIntegralMakeTasksNumber(int taskSize, int integralCanGet) {
        LogUtil.d(TAG, "showIntegralMakeTasksNumber is:" + taskSize + ";" + integralCanGet);
        if (showIntegralMake()) {
            if (taskSize != 0) {
                mItemInfo3.description = String.format(ResourceUtil.getString(this, "uc_txt_member_integral_make_desription"), taskSize, integralCanGet);
            } else {
                mItemInfo3.description = ResourceUtil.getString(this, "uc_user_center_member_integral_make_description_empty");
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private static class ItemAdapter extends BaseAdapter {
        private List<ItemInfo> mItemInfos;
        private Context mContext;

        public ItemAdapter(Context context) {
            this.mContext = context;
        }

        public void setData(List<ItemInfo> itemInfos) {
            if (itemInfos != null) {
                this.mItemInfos = itemInfos;
            } else {
                this.mItemInfos = new ArrayList<ItemInfo>();
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
                convertView = LayoutInflater.from(mContext).inflate(getLayoutId(mContext, "uc_list_item_member_integral"), null);
                viewHolder = new ViewHolder();
                viewHolder.imgIcon = getView(convertView, getWidgetId(mContext, "img_list_item_member_privilege"));
                viewHolder.txtName = getView(convertView, getWidgetId(mContext, "txt_list_item_member_privilege_name"));
                viewHolder.txtDescription = getView(convertView, getWidgetId(mContext, "txt_list_item_member_privilege_descrition"));
                viewHolder.viewLine = getView(convertView, getWidgetId(mContext, "view_list_item_member_privilege_line"));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ItemInfo itemInfo = getItem(position);
            viewHolder.imgIcon.setImageResource(itemInfo.iconResId);
            viewHolder.txtName.setText(mContext.getString(itemInfo.nameResId));
            viewHolder.txtDescription.setText(itemInfo.description);
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
            TextView txtDescription;
            View viewLine;
        }
    }

    private static class ItemInfo {
        int id;
        int iconResId;
        String description;
        int nameResId;

        public ItemInfo(int id, int iconResId, int nameResId) {
            this.id = id;
            this.iconResId = iconResId;
            this.nameResId = nameResId;
        }
    }

}
