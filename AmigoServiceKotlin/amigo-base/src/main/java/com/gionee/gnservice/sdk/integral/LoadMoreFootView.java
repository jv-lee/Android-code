package com.gionee.gnservice.sdk.integral;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.widget.fresh.IBottomView;

/**
 * Created by caocong on 6/15/17.
 */
public class LoadMoreFootView extends FrameLayout implements IBottomView {
    private static final String TAG = LoadMoreFootView.class.getSimpleName();
    private ProgressBar mLoadingView;
    private TextView mTxtView;
    private ImageView mPullArrow;
    private boolean mNoMore = false;

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        return super.dispatchKeyEventPreIme(event);
    }

    public LoadMoreFootView(Context context) {
        this(context, null);
    }

    public LoadMoreFootView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadMoreFootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(ResourceUtil.getLayoutId(getContext(), "uc_listview_load_more"), null);
        mLoadingView = (ProgressBar) rootView.findViewById(ResourceUtil.getWidgetId(getContext(), "progress_listview_load_more"));
        mPullArrow = (ImageView) rootView.findViewById(ResourceUtil.getWidgetId(getContext(), "img_listview_load_more_arrow"));
        mTxtView = (TextView) rootView.findViewById(ResourceUtil.getWidgetId(getContext(), "uc_txt_listview_load_more"));
        mTxtView.setText(ResourceUtil.getString(getContext(), "uc_txt_listview_load_more_loading"));
        addView(rootView);
    }

    public void setNoMore(boolean value) {
        LogUtil.d(TAG, "set no more: mNoMore = " + value);
        mNoMore = value;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingUp(float fraction, float maxBottomHeight, float bottomHeight) {
        LogUtil.d(TAG, "onPullingUp");
    }

    @Override
    public void startAnim(float maxBottomHeight, float bottomHeight) {
        LogUtil.d(TAG, "startAnim");
        if (!mNoMore) {
            mLoadingView.setVisibility(View.VISIBLE);
            mPullArrow.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPullReleasing(float fraction, float maxBottomHeight, float bottomHeight) {
        LogUtil.d(TAG, "onPullReleasing");
        if (mNoMore) {
            mTxtView.setText(ResourceUtil.getString(getContext(), "uc_txt_listview_load_more_load_all"));
        } else {
            mTxtView.setText(ResourceUtil.getString(getContext(), "uc_txt_listview_load_more_loading"));
        }
    }

    @Override
    public void onFinish() {
        LogUtil.d(TAG, "onFinish");
        mLoadingView.setVisibility(View.GONE);
        if (!mNoMore) {
            mPullArrow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void reset() {
        //mNoMore = false;
        //mTxtView.setText(ResourceUtil.getString(getContext(), "uc_txt_listview_load_more_loading"));
    }
}
