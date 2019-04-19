package com.lee.code;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lee.library.base.BaseActivity;
import com.lee.library.ioc.annotation.ContentView;
import com.lee.library.ioc.annotation.InjectView;
import com.lee.library.livedatabus.InjectBus;
import com.lee.library.livedatabus.LiveDataBus;

@ContentView(R.layout.activity_inject)
public class InjectActivity extends BaseActivity {

    @InjectView(R.id.fl_container)
    FrameLayout flContainer;

    @Override
    protected void bindView() {
        getSupportFragmentManager().beginTransaction().add(flContainer.getId(),new InjectFragment()).commit();

    }

    @Override
    public void bindData(Bundle savedInstanceState) {
        LiveDataBus.getInstance().getChannel("event").postValue("你好");
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
