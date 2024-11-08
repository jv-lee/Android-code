package com.lee.library.mvp;


import android.os.Bundle;
import android.support.annotation.Nullable;


import com.lee.library.base.BaseActivity;

/**
 * @author jv.lee
 * @date 2019/5/6
 */
public abstract class BaseMvpActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MvpManager.injectPresenter(this);
        super.onCreate(savedInstanceState);
    }
}
