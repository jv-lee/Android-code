package com.lee.app.mvp;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lee.app.base.BaseActivity;

/**
 * @author jv.lee
 * @date 2019/5/6
 */
public abstract class BaseMvpFullActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MvpManager.injectPresenter(this);
        super.onCreate(savedInstanceState);
    }
}
