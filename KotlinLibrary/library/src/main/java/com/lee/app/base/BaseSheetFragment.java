package com.lee.app.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;

import com.lee.app.R;
import com.lee.app.ioc.InjectManager;

/**
 * @author jv.lee
 */
public abstract class BaseSheetFragment extends BottomSheetDialogFragment {
    protected BaseActivity mActivity;
    protected FragmentManager mFragmentManager;
    protected BottomSheetBehavior mBehavior;
    private boolean isVisibleUser = false;
    private boolean isVisibleView = false;
    private boolean fistVisible = true;

    public BaseSheetFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View root = InjectManager.injectLayout(this);
        dialog.setContentView(root);
        mBehavior = BottomSheetBehavior.from((View) root.getParent());
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        mFragmentManager = getChildFragmentManager();
        super.onActivityCreated(savedInstanceState);
        InjectManager.injectViews(this);
        bindData(savedInstanceState);
        bindView();
        InjectManager.injectEvents(this);
        isVisibleView = true;
        if (isVisibleUser && fistVisible) {
            fistVisible = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisibleUser = true;
            onFragmentResume();
            //首次用户可见 开始加载数据
            if (isVisibleView && isVisibleUser && fistVisible) {
                fistVisible = false;
            }
        } else {
            isVisibleUser = false;
            onFragmentPause();
        }
    }

    protected void onFragmentResume() {
    }

    protected void onFragmentPause() {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * 设置加载数据等业务操作
     *
     * @param savedInstanceState 重置回调参数
     */
    protected abstract void bindData(Bundle savedInstanceState);

    /**
     * 设置view基础配置
     */
    protected abstract void bindView();

}
