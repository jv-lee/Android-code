package com.lee.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.widget.ContentLoadingProgressBar;

import com.lee.library.R;

/**
 * @author jv.lee
 * @description 状态栏Layout
 */
public class StatusLayout extends FrameLayout {

    /**
     * 状态：加载状态
     */
    public static final int STATUS_LOADING = 1;

    /**
     * 状态：数据展示
     */
    public static final int STATUS_DATA = 2;

    /**
     * 状态：错误数据
     */
    public static final int STATUS_DATA_ERROR = 3;

    /**
     * 状态：无网络
     */
    public static final int STATUS_NOT_NETWORK = 4;

    /**
     * 状态：空数据
     */
    public static final int STATUS_EMPTY_DATA = 5;

    /**
     * 状态布局ResourceId
     */
    private int loadingId, errorId, networkId, emptyId;

    /**
     * 状态布局 视图View
     */
    private View loadingView, errorView, networkView, emptyView;

    private View errorRestart, networkRestart;
    private ContentLoadingProgressBar loadingProgressBar;

    /**
     * 重新加载回调接口
     * {@link OnReloadListener}
     */
    private OnReloadListener onReloadListener;

    public StatusLayout(Context context) {
        this(context, null);
    }

    public StatusLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttrs(context, attrs);
        initView();
        initListener();
        initStatus();
    }

    private void readAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusLayout);
        loadingId = typedArray.getResourceId(R.styleable.StatusLayout_layout_loading, R.layout.layout_status_loading);
        errorId = typedArray.getResourceId(R.styleable.StatusLayout_layout_error, R.layout.layout_status_error);
        networkId = typedArray.getResourceId(R.styleable.StatusLayout_layout_network, R.layout.layout_status_not_network);
        emptyId = typedArray.getResourceId(R.styleable.StatusLayout_layout_empty, R.layout.layout_status_empty);
        typedArray.recycle();
    }

    @SuppressLint("InflateParams")
    private void initView() {
        loadingView = LayoutInflater.from(getContext()).inflate(loadingId, null);
        errorView = LayoutInflater.from(getContext()).inflate(errorId, null);
        networkView = LayoutInflater.from(getContext()).inflate(networkId, null);
        emptyView = LayoutInflater.from(getContext()).inflate(emptyId, null);
        loadingProgressBar = loadingView.findViewById(R.id.progress);
        errorRestart = errorView.findViewById(R.id.btn_restart);
        networkRestart = networkView.findViewById(R.id.btn_restart);
        addView(loadingView);
        addView(errorView);
        addView(networkView);
        addView(emptyView);
    }

    private void initListener() {
        errorRestart.setOnClickListener(v -> {
            if (onReloadListener != null) {
                onReloadListener.onReload();
            }
        });
        networkRestart.setOnClickListener(v -> {
            if (onReloadListener != null) {
                onReloadListener.onReload();
            }
        });
    }

    /**
     * 隐藏所有布局
     */
    private void initStatus() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(GONE);
        }
    }

    /**
     * 显示数据展示
     */
    private void showData() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt != loadingView && childAt != errorView && childAt != networkView && childAt != emptyView) {
                childAt.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * 设置显示状态
     *
     * @param status 状态码
     */
    public void setStatus(int status) {
        initStatus();
        switch (status) {
            case STATUS_LOADING:
                loadingView.setVisibility(VISIBLE);
                break;
            case STATUS_DATA_ERROR:
                errorView.setVisibility(VISIBLE);
                break;
            case STATUS_NOT_NETWORK:
                networkView.setVisibility(VISIBLE);
                break;
            case STATUS_EMPTY_DATA:
                emptyView.setVisibility(VISIBLE);
                break;
            case STATUS_DATA:
                showData();
                break;
            default:
                break;
        }
    }

    public void setLoadingProgressColor(int color) {
        loadingProgressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public interface OnReloadListener {
        /**
         * 重新加载数据
         */
        void onReload();
    }

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }
}
