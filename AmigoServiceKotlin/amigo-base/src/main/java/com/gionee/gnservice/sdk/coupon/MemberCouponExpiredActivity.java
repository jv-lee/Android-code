package com.gionee.gnservice.sdk.coupon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gionee.gnservice.base.BaseSdkActivity;
import com.gionee.gnservice.entity.CouponInfo;
import com.gionee.gnservice.sdk.integral.LoadMoreFootView;
import com.gionee.gnservice.utils.ChameleonColorManager;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.ToastUtil;
import com.gionee.gnservice.widget.fresh.RefreshListenerAdapter;
import com.gionee.gnservice.widget.fresh.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import static com.gionee.gnservice.utils.ResourceUtil.getDrawableId;
import static com.gionee.gnservice.utils.ResourceUtil.getWidgetId;

/**
 * Created by caocong on 1/9/17.
 */
public class MemberCouponExpiredActivity extends BaseSdkActivity implements IMemberCouponContract.View {

    private static final String TAG = MemberCouponActivity.class.getSimpleName();
    private TwinklingRefreshLayout mRefreshLayout;
    private ListView mRecyclerView;
    private ItemAdapter mAdapter;
    private MemberCouponExpirePresenter mPresenter;
    private List<CouponInfo> mCouponInfos;
    private ViewGroup mLayoutEmpty;
    private View mLoadingView;
    private LoadMoreFootView mFootView;
    private int mPage = 1;
    private static final int PAGE_NUMBER = 20;
    private static final int TYPE_LOAD_MORE = 0;
    private static final int TYPE_HEAD_UPDATE = 1;
    private int mLoadType;

    @Override
    protected void initView() {
        mRefreshLayout = getView(getWidgetId(this, "swipe_layout_member_coupon"));
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setFloatRefresh(true);
        mFootView = new LoadMoreFootView(this);
        mRefreshLayout.setBottomView(mFootView);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mLoadType = TYPE_HEAD_UPDATE;
                mPage = 1;
                loadData();
                mFootView.setNoMore(false);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mLoadType = TYPE_LOAD_MORE;
                loadData();
            }
        });
        mLoadingView = getView(getWidgetId(this, "progress_member_coupon_record"));

        mRecyclerView = getView(getWidgetId(this, "recyleview_member_coupon"));
        mAdapter = new ItemAdapter(this, mCouponInfos);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutEmpty = getView(getWidgetId(this, "layout_member_coupon_empty"));
        TextView expiredCouPonEmpty = getView(getWidgetId(this, "uc_txt_member_coupon_empty_tv"));
        expiredCouPonEmpty.setText(ResourceUtil.getString(this, "uc_txt_member_expired_coupon_empty"));
    }

    @Override
    protected void initData() {
        super.initData();
        mLoadingView.setVisibility(View.VISIBLE);
        loadData();
    }

    private void loadData() {
        if (ToastUtil.showNetWorkErrorToast(this)) {
            mLoadingView.setVisibility(View.GONE);
            return;
        }
        if (mPresenter != null) {
            mPresenter.loadCoupons(mPage, PAGE_NUMBER);
        }
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        mCouponInfos = new ArrayList<CouponInfo>();
        mPresenter = new MemberCouponExpirePresenter(appContext(), this);
    }

    @Override
    protected void addChameleonColorView() {
        getView(getWidgetId(this, "layout_member_coupon")).setBackgroundColor(ChameleonColorManager.getBackgroudColor_B1());
    }

    @Override
    protected int getLayoutResId() {
        return ResourceUtil.getLayoutId(this, "uc_activity_member_coupon");
    }

    @Override
    protected String getActionbarTitle() {
        return getString(ResourceUtil.getStringId(this, "uc_title_member_coupon_expired_actionbar"));
    }


    @Override
    public void showLoadStartView() {
    }

    @Override
    public void showCouponView(List<CouponInfo> couponInfos) {
        mLoadingView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        LogUtil.d(TAG, "get coupon list is:" + couponInfos);
        switch (mLoadType) {
            case TYPE_HEAD_UPDATE:
                if (couponInfos.isEmpty()) {
                    showEmptyView();
                } else {
                    ++mPage;
                    mCouponInfos.clear();
                    mCouponInfos.addAll(couponInfos);
                    mAdapter.notifyDataSetChanged();
                }
                mRefreshLayout.finishRefreshing();
                break;
            case TYPE_LOAD_MORE:
                if (couponInfos.isEmpty()) {
                    if (mCouponInfos.isEmpty()) {
                        showEmptyView();
                    } else {
                        mFootView.setNoMore(true);
                    }
                } else {
                    ++mPage;
                    mCouponInfos.addAll(couponInfos);
                    mAdapter.notifyDataSetChanged();

                }
                mRefreshLayout.finishLoadmore();
                break;
            default:
                break;
        }
    }

    private void showEmptyView() {
        mLayoutEmpty.setVisibility(View.VISIBLE);
        mRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLoadFailView() {
        ToastUtil.showNetWorkErrorToast(this);
        mLoadingView.setVisibility(View.GONE);
    }


    private static class ItemAdapter extends BaseAdapter {
        private static final int STATUS_USED = 0;
        private static final int STATUS_STANDARD = 1;
        private List<CouponInfo> dataList;
        private Context mContext;

        public ItemAdapter(Context context, List<CouponInfo> dataList) {
            this.mContext = context;
            this.dataList = dataList;
        }


        private String getFormatDataString(String dataString) {
            return dataString.substring(0, 4) + "." + dataString.substring(4, 6) + "." + dataString.substring(6, 8);
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public CouponInfo getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            CouponInfo couponInfo = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(ResourceUtil.getLayoutId(mContext, "uc_list_item_member_coupon"), null);
                viewHolder = new ViewHolder();
                viewHolder.layoutUsed = getView(convertView, getWidgetId(mContext, "layout_list_item_coupon_used"));
                viewHolder.layoutUnuse = getView(convertView, getWidgetId(mContext, "layout_list_item_coupon_unuse"));
                viewHolder.layoutUnuse.setVisibility(View.GONE);
                viewHolder.layoutUsed.setVisibility(View.VISIBLE);
                viewHolder.txtNumber = getView(convertView, getWidgetId(mContext, "txt_list_item_member_coupon_number_used"));
                viewHolder.txtDescription = getView(convertView, getWidgetId(mContext, "txt_list_item_member_coupon_description_used"));
                viewHolder.txtType = getView(convertView, getWidgetId(mContext, "txt_list_item_member_coupon_type_used"));
                viewHolder.txtName = getView(convertView, getWidgetId(mContext, "txt_list_item_member_coupon_name_used"));
                viewHolder.txtDuration = getView(convertView, getWidgetId(mContext, "txt_list_item_member_coupon_duration_used"));
                viewHolder.txtStatus = getView(convertView, getWidgetId(mContext, "txt_list_item_member_coupon_status_used"));
                viewHolder.txtLimit = getView(convertView, getWidgetId(mContext, "txt_list_item_member_coupon_number_limit_used"));
                viewHolder.txtStatus.setBackgroundResource(getDrawableId(mContext, "uc_bg_member_coupon_list_expired"));
                viewHolder.bottomLine = getView(convertView, getWidgetId(mContext, "view_list_item_member_integral_record_line_last"));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            switch (couponInfo.getType()) {
                case CouponInfo.TYPE_GAME:
                    viewHolder.txtType.setText(ResourceUtil.getString(mContext, "uc_txt_member_coupon_type_game"));
                    break;
                case CouponInfo.TYPE_MUSIC:
                    viewHolder.txtType.setText(ResourceUtil.getString(mContext, "uc_txt_member_coupon_type_music"));
                    break;
                case CouponInfo.TYPE_THEME:
                    viewHolder.txtType.setText(ResourceUtil.getString(mContext, "uc_txt_member_coupon_type_theme"));
                    break;
                case CouponInfo.TYPE_COUPON:
                default:
                    viewHolder.txtType.setText(ResourceUtil.getString(mContext, "uc_txt_member_coupon_type_default"));
                    break;
            }
            viewHolder.txtName.setText(couponInfo.getTaskId());
            viewHolder.txtName.setText(couponInfo.getTaskId());
            if (couponInfo.getDenomination() == 0) {
                viewHolder.txtNumber.setText("0");
            } else {
                viewHolder.txtNumber.setText(String.valueOf(couponInfo.getDenomination()));
            }
            viewHolder.txtDescription.setText(couponInfo.getUserAppName());
            viewHolder.txtDuration.setText(getFormatDataString(couponInfo.getStartTime()) + "-" + getFormatDataString(couponInfo.getEndTime()));
            if (couponInfo.getUseAmount() == 0) {
                viewHolder.txtLimit.setText(ResourceUtil.getString(mContext, "uc_txt_member_coupon_type_no_limit"));
            } else {
                viewHolder.txtLimit.setText(String.format(ResourceUtil.getString(mContext, "uc_txt_member_coupon_type_need_num"), String.valueOf(couponInfo.getUseAmount())));
            }
            if (position == getCount() - 1) {
                viewHolder.bottomLine.setVisibility(View.VISIBLE);
            } else {
                viewHolder.bottomLine.setVisibility(View.GONE);
            }
            return convertView;
        }

        @SuppressWarnings("unused")
        <T extends View> T getView(View view, int id) {
            return (T) view.findViewById(id);
        }

    }

    private static class ViewHolder {
        private View bottomLine;
        private ViewGroup layoutUsed, layoutUnuse;
        private TextView txtNumber, txtType, txtName, txtDescription, txtDuration, txtStatus, txtLimit;
    }

}
