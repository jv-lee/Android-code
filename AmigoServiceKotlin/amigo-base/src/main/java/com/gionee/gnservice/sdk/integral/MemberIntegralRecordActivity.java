package com.gionee.gnservice.sdk.integral;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gionee.gnservice.base.BaseSdkActivity;
import com.gionee.gnservice.entity.IntegralRecord;
import com.gionee.gnservice.sdk.integral.mvp.IMemberIntegralRecordContract;
import com.gionee.gnservice.sdk.integral.mvp.MemberIntegralRecordPresenter;
import com.gionee.gnservice.utils.ChameleonColorManager;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.ToastUtil;
import com.gionee.gnservice.widget.fresh.RefreshListenerAdapter;
import com.gionee.gnservice.widget.fresh.TwinklingRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.gionee.gnservice.utils.ResourceUtil.getLayoutId;
import static com.gionee.gnservice.utils.ResourceUtil.getStringId;
import static com.gionee.gnservice.utils.ResourceUtil.getWidgetId;

/**
 * Created by caocong on 1/9/17.
 */
public class MemberIntegralRecordActivity extends BaseSdkActivity implements IMemberIntegralRecordContract.View {
    private static final String TAG = MemberIntegralRecordActivity.class.getSimpleName();
    //获取数据类型
    public static final int TYPE_INTEGRAL_RECORD = 0;
    public static final int TYPE_OBTAIN_RECORD = 1;
    private ListView mListMenu;
    private ItemAdapter mAdapter;
    private List<IntegralRecord> mIntegralRecords;
    private TwinklingRefreshLayout mSwipeLayout;
    private TextView mTxtUnuse;
    private IMemberIntegralRecordContract.Presenter mPresenter;
    private View mLoadingView;
    private int mPage;
    private int mUserIntegral;
    private int mType;
    private int mLoadType;
    private View mLineTop, mlineBottom;
    private ViewGroup mLayoutEmpty;
    private ViewGroup mLayoutInfo;
    private LoadMoreFootView mMoreFootView;
    //加载类型
    private static final int TYPE_LOAD_MORE = 0;
    private static final int TYPE_HEAD_UPDATE = 1;

    @Override
    protected void initActionBar() {
        super.initActionBar();
    }


    @Override
    protected void initVariables() {
        super.initVariables();
        mIntegralRecords = new ArrayList<IntegralRecord>();
        mPresenter = new MemberIntegralRecordPresenter(appContext(), this);

        Intent intent = getIntent();
        mType = intent.getIntExtra("type", TYPE_INTEGRAL_RECORD);
        mUserIntegral = intent.getIntExtra("user_integral", 0);
        LogUtil.d(TAG, "user integral is:" + mUserIntegral);
    }

    @Override
    protected void initView() {

        mPage = 1;
        mSwipeLayout = getView(getWidgetId(this, "swipe_layout_member_integral_record"));
        mMoreFootView = new LoadMoreFootView(this);
        mSwipeLayout.setBottomView(mMoreFootView);
        mSwipeLayout.setEnableRefresh(false);
        mSwipeLayout.setEnableLoadmore(true);
        mSwipeLayout.setFloatRefresh(true);
        mSwipeLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                LogUtil.d(TAG, "onRefresh");
                mLoadType = TYPE_HEAD_UPDATE;
                mPage = 1;
                loadData();
                mMoreFootView.setNoMore(false);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                LogUtil.d(TAG, "onLoadMore");
                mLoadType = TYPE_LOAD_MORE;
                loadData();
            }

        });
        mTxtUnuse = getView(getWidgetId(this, "txt_member_integral_record_unuse"));
        mLoadingView = getView(getWidgetId(this, "progress_member_integral_record"));
        mLayoutInfo = getView(getWidgetId(this, "layout_member_integral_info"));
        if (mType == TYPE_INTEGRAL_RECORD) {
            mTxtUnuse.setText(String.valueOf(mUserIntegral));
        } else {
            mLayoutInfo.setVisibility(View.GONE);
        }

        mListMenu = getView(getWidgetId(this, "recyleview_member_integral_record"));

        mAdapter = new ItemAdapter(this, mIntegralRecords);
        mListMenu.setAdapter(mAdapter);
        mLineTop = getView(ResourceUtil.getWidgetId(this, "line_member_integral_record_top"));
        mlineBottom = getView(ResourceUtil.getWidgetId(this, "line_member_integral_record_bottom"));
        mLineTop.setVisibility(View.GONE);
        mlineBottom.setVisibility(View.GONE);

        mLayoutEmpty = getView(ResourceUtil.getWidgetId(this, "layout_member_integral_record_empty"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void initData() {
        super.initData();
        mLoadingView.setVisibility(View.VISIBLE);
        loadData();
    }

    private void loadData() {
        LogUtil.d(TAG, "load data type is:" + mType);
        if (ToastUtil.showNetWorkErrorToast(this)) {
            mLoadingView.setVisibility(View.GONE);
            return;
        }
        if (mType == TYPE_INTEGRAL_RECORD) {
            mPresenter.loadIntegralRecords(mPage);
        } else if (mType == TYPE_OBTAIN_RECORD) {
            mPresenter.loadObtainRecords(mPage);
        }
    }

    protected void addChameleonColorView() {
        getView(ResourceUtil.getWidgetId(this, "flayout_electronic_warranty")).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
        mListMenu.setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
    }

    @Override
    protected String getActionbarTitle() {
        if (mType == TYPE_INTEGRAL_RECORD) {
            return getResources().getString(getStringId(this, "uc_title_member_integral_record_actionbar"));
        } else {
            return getResources().getString(getStringId(this, "uc_title_member_integral_obtain_record_actionbar"));
        }
    }

    @Override
    protected int getLayoutResId() {
        return getLayoutId(this, "uc_activity_member_integral_record");
    }

    @Override
    public void showIntegralRecordView(List<IntegralRecord> integralRecords) {
        LogUtil.d(TAG, "integral records list is:" + integralRecords.toString());
        mLoadingView.setVisibility(View.GONE);
        mSwipeLayout.setEnableLoadmore(true);
        mSwipeLayout.setEnableRefresh(false);
        mPresenter.loadIntegralUser();
        switch (mLoadType) {
            case TYPE_HEAD_UPDATE:
                if (integralRecords.isEmpty()) {
                    showEmptyView();
                } else {
                    ++mPage;
                    mIntegralRecords.clear();
                    mIntegralRecords.addAll(integralRecords);
                    mAdapter.notifyDataSetChanged();
                    mLayoutInfo.setVisibility(View.VISIBLE);
                }
                mSwipeLayout.finishRefreshing();
                break;
            case TYPE_LOAD_MORE:
                if (integralRecords.isEmpty()) {
                    if (mIntegralRecords.isEmpty()) {
                        showEmptyView();
                    } else {
                        mMoreFootView.setNoMore(true);
                        mAdapter.setNoMore(true);
                    }
                } else {
                    ++mPage;
                    mIntegralRecords.addAll(integralRecords);
                    mAdapter.notifyDataSetChanged();
                    mLayoutInfo.setVisibility(View.VISIBLE);

                }
                mSwipeLayout.finishLoadmore();
                break;
            default:
                break;
        }
    }

    public void showEmptyView() {
        mLayoutEmpty.setVisibility(View.VISIBLE);
        mSwipeLayout.setVisibility(View.GONE);
        mLayoutInfo.setVisibility(View.GONE);
    }

    @Override
    public void showLoadFailView() {
        LogUtil.d(TAG, "showLoadFailView");
        ToastUtil.showLong(this, ResourceUtil.getString(this, "uc_network_exception"));
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void showIntegralUserView(int value) {
        mTxtUnuse.setText(String.valueOf(value));
    }

    private void addDataItem(List<IntegralRecord> integralRecords) {
        mIntegralRecords.addAll(integralRecords);
    }

    private void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    private static class ItemAdapter extends BaseAdapter {

        boolean mNoMore;
        private Context mContext;
        List<IntegralRecord> dataList;

        public ItemAdapter(Context context, List<IntegralRecord> dataList) {
            mNoMore = false;
            mContext = context;
            this.dataList = dataList;
        }

        public void setNoMore(boolean noMore) {
            mNoMore = noMore;
        }

        private String getStringDate(String timeMills) {
            try {
                Date currentTime = new Date(Long.parseLong(timeMills));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = formatter.format(currentTime);
                return dateString;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public IntegralRecord getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(ResourceUtil.getLayoutId(mContext, "uc_list_item_member_integral_record"), null);
                viewHolder = new ViewHolder();
                viewHolder.txtName = getView(convertView, getWidgetId(mContext, "txt_list_item_member_integral_record_name"));
                viewHolder.txtTime = getView(convertView, getWidgetId(mContext, "txt_list_item_member_integral_record_time"));
                viewHolder.txtSource = getView(convertView, getWidgetId(mContext, "txt_list_item_member_integral_record_source"));
                viewHolder.txtLine = getView(convertView, getWidgetId(mContext, "txt_list_item_member_integral_record_line"));
                viewHolder.txtNumber = getView(convertView, getWidgetId(mContext, "txt_list_item_member_integral_record_number"));
                viewHolder.viewLine = getView(convertView, getWidgetId(mContext, "view_list_item_member_integral_record_line"));
                viewHolder.viewLineLast = getView(convertView, getWidgetId(mContext, "view_list_item_member_integral_record_line_last"));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            IntegralRecord record = getItem(position);
            LogUtil.d(TAG, "record is null=" + (record == null));
            if (record == null) {
                return convertView;
            }
            viewHolder.txtName.setText(record.getAction());

            int num = record.getScore();
            if (num >= 0) {
                viewHolder.txtNumber.setText("+" + num);
            } else {
                viewHolder.txtNumber.setText(String.valueOf(num));
            }
            if (!ChameleonColorManager.isPowerSavingMode()) {
                if (num >= 0) {

                    viewHolder.txtNumber.setTextColor(ResourceUtil.getColor(mContext, "uc_member_privilege_record_number_positive"));
                } else {

                    viewHolder.txtNumber.setTextColor(ResourceUtil.getColor(mContext, "uc_member_privilege_record_number_negative"));
                }
            }
            viewHolder.txtSource.setText(record.getAppName());
            viewHolder.txtLine.setText("  |  ");
            viewHolder.txtTime.setText(getStringDate(record.getCreateTime()));
            if (position == getCount() - 1) {
                viewHolder.viewLine.setVisibility(View.GONE);
                viewHolder.viewLineLast.setVisibility(View.VISIBLE);
            } else {
                viewHolder.viewLine.setVisibility(View.VISIBLE);
                viewHolder.viewLineLast.setVisibility(View.GONE);
            }
            return convertView;
        }

        @SuppressWarnings("unused")
        <T extends View> T getView(View view, int id) {
            return (T) view.findViewById(id);
        }
    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtTime, txtSource, txtLine;
        TextView txtNumber;
        View viewLine, viewLineLast;
    }
}
