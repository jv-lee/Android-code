package com.lee.status;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class StatusLayout extends FrameLayout {

    public static final int STATUS_LOADING = 1;
    public static final int STATUS_DATA = 2;
    public static final int STATUS_DATA_ERROR = 3;
    public static final int STATUS_NOT_NETWORK = 4;

    private View loadingView;
    private View errorView;
    private View networkView;

    private Button errorRestart,networkRestart;
    private ContentLoadingProgressBar loadingProgressBar;

    public StatusLayout( Context context) {
        this(context,null);
    }

    public StatusLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StatusLayout( Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initListener();
        initStatus();
    }

    private void initView() {
        loadingView = LayoutInflater.from(getContext()).inflate(R.layout.view_status_loading, null);
        errorView = LayoutInflater.from(getContext()).inflate(R.layout.view_status_error, null);
        networkView = LayoutInflater.from(getContext()).inflate(R.layout.view_status_not_network, null);
        loadingProgressBar = loadingView.findViewById(R.id.progress);
        errorRestart = errorView.findViewById(R.id.btn_restart);
        networkRestart = networkView.findViewById(R.id.btn_restart);
        addView(loadingView);
        addView(errorView);
        addView(networkView);
    }

    private void initListener() {
        errorRestart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRestartListener != null) {
                    onRestartListener.onRestart();
                }
            }
        });
        networkRestart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRestartListener != null) {
                    onRestartListener.onRestart();
                }
            }
        });
    }

    private void initStatus() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(GONE);
        }
    }

    private void showData(){
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt != loadingView && childAt != errorView && childAt != networkView) {
                childAt.setVisibility(VISIBLE);
            }
        }
    }

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
            case STATUS_DATA:
                showData();
                break;
        }
    }

    public void setLoadingProgressColor(int color) {
        loadingProgressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public interface OnRestartListener{
        void onRestart();
    }

    private OnRestartListener onRestartListener;

    public void setOnRestartListener(OnRestartListener onRestartListener) {
        this.onRestartListener = onRestartListener;
    }
}
