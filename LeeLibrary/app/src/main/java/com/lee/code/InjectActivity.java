package com.lee.code;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.lee.library.base.BaseActivity;
import com.lee.library.ioc.annotation.ContentView;
import com.lee.library.ioc.annotation.InjectView;

@ContentView(R.layout.activity_inject)
public class InjectActivity extends BaseActivity {

    @InjectView(R.id.fl_container)
    FrameLayout flContainer;

    @Override
    public void bindData(Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction().add(flContainer.getId(),new InjectFragment()).commit();
    }

    @Override
    public View toolbar() {
        return null;
    }

    @Override
    public boolean isFullscreen() {
        return false;
    }


}
