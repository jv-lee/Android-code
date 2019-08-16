package com.gionee.gnservice.sdk.integral;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.widget.fresh.IHeaderView;
import com.gionee.gnservice.widget.fresh.OnAnimEndListener;

/**
 * Created by caocong on 6/15/17.
 */
public class LoadFreshHeaderView extends FrameLayout implements IHeaderView {
    private static final String TAG = LoadFreshHeaderView.class.getSimpleName();
    private View mLoadingView;
    private TextView mTxtView;
    private ImageView mImgArrowView;
    private String mPullDownStr, mRefreshingStr, mReleaseToRefresh;

    public LoadFreshHeaderView(Context context) {
        this(context, null);
    }

    public LoadFreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadFreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRefreshingStr = ResourceUtil.getString(context, "uc_txt_listview_load_more_freshing");
        mPullDownStr = ResourceUtil.getString(context, "uc_txt_listview_load_more_pull_freshing");
        mReleaseToRefresh = ResourceUtil.getString(context, "uc_txt_listview_load_more_pull_release_to_fresh");
        initView();
    }

    private void initView() {

        Context context = getContext();
        View rootView = LayoutInflater.from(getContext()).inflate(ResourceUtil.getLayoutId(context, "uc_listview_load_more"), null);
        mLoadingView = rootView.findViewById(ResourceUtil.getWidgetId(context, "progress_listview_load_more"));
        mTxtView = (TextView) rootView.findViewById(ResourceUtil.getWidgetId(context, "uc_txt_listview_load_more"));
        mImgArrowView = (ImageView) rootView.findViewById(ResourceUtil.getWidgetId(context, "img_listview_load_more_arrow"));

        addView(rootView);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        LogUtil.d(TAG, "onPullingDown");
        if (fraction<1f){
            mTxtView.setText(mPullDownStr);
        }else {
            mTxtView.setText(mReleaseToRefresh);
        }
        mImgArrowView.setRotation(fraction * headHeight / maxHeadHeight * 180);

    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        LogUtil.d(TAG, "onPullReleasing");
        if (fraction < 1f) {
            mTxtView.setText(mPullDownStr);
            mImgArrowView.setRotation(fraction * headHeight / maxHeadHeight * 180);
            if (mImgArrowView.getVisibility() == GONE) {
                mImgArrowView.setVisibility(VISIBLE);
                mLoadingView.setVisibility(GONE);
            }
        }
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        LogUtil.d(TAG, "startAnim");
        mTxtView.setText(mRefreshingStr);
        mImgArrowView.setVisibility(GONE);
        mLoadingView.setVisibility(VISIBLE);
    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        LogUtil.d(TAG, "onFinish");
        animEndListener.onAnimEnd();
    }

    @Override
    public void reset() {
        LogUtil.d(TAG, "reset");
        mImgArrowView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mTxtView.setText(mPullDownStr);
    }
}
