package com.lee.code.ioc;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lee.code.ioc.base.BaseActivity;
import com.lee.library.ioc.InjectManager;
import com.lee.library.ioc.adapter.RView;
import com.lee.library.ioc.annotation.ContentView;
import com.lee.library.ioc.annotation.InjectView;
import com.lee.library.ioc.annotation.OnItemClick;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_recyler)
public class RecyclerActivity extends BaseActivity {

    @InjectView(R.id.rv_container)
    RView rvContainer;

    @Override
    public void bindData(Bundle savedInstanceState) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("this is item index: " + i);
        }
        rvContainer.setLayoutManager(new LinearLayoutManager(this));
        rvContainer.setRViewAdapter(new RvAdapter(data));
        InjectManager.injectEvents(this);
    }

    @OnItemClick(values = {R.id.rv_container})
    public void onItemClick(View view, String string,int position) {
        Toast.makeText(this, "position:"+position +" data:"+string, Toast.LENGTH_SHORT).show();
    }


}
