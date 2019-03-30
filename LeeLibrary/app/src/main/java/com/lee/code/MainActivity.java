package com.lee.code;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lee.code.adapter.MultiAdapter;
import com.lee.code.bean.UserInfo;
import com.lee.library.adapter.LeeRecyclerView;
import com.lee.library.base.BaseActivity;
import com.lee.library.ioc.InjectManager;
import com.lee.library.ioc.annotation.ContentView;
import com.lee.library.ioc.annotation.InjectView;
import com.lee.library.ioc.annotation.OnItemClick;
import com.lee.library.ioc.annotation.OnItemLongClick;
import com.lee.library.ioc.annotation.OnLoadingMore;
import com.lee.library.ioc.annotation.OnRefresh;
import com.lee.library.refresh.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @InjectView(R.id.container)
    FrameLayout container;
    @InjectView(R.id.rv_container)
    LeeRecyclerView rvContainer;
    @InjectView(R.id.refresh)
    RefreshLayout refresh;

    MultiAdapter multiAdapter;
    private List<UserInfo> data = new ArrayList<>();

    @Override
    public void bindData(Bundle savedInstanceState) {
        multiAdapter = new MultiAdapter(data);
        rvContainer.setLayoutManager(new LinearLayoutManager(this));
        rvContainer.setRecyclerAdapter(multiAdapter);
        refresh.setDefaultView(container, rvContainer);
        initData();
        InjectManager.injectEvents(this);
    }

    private void initData() {
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= 15; j++) {
                UserInfo user = new UserInfo();
                if (j % 15 == 1) {
                    user.setType(1);
                    user.setAccount("lee >>>>>> 11111 >>>>>");
                } else if (j % 15 == 2 || j % 15 == 3) {
                    user.setType(2);
                    user.setAccount(" lee >>>>> 22222 >>>>>");
                } else if (j % 15 == 4 || j % 15 == 5 || j % 15 == 6) {
                    user.setType(3);
                    user.setAccount("lee >>>>> 33333 >>>>>>");
                } else if (j % 15 == 7 || j % 15 == 8 || j % 13 == 9 || j % 15 == 0) {
                    user.setType(4);
                    user.setAccount("lee >>>>> 44444 >>>>>");
                } else {
                    user.setType(5);
                    user.setAccount("lee >>>>> 55555 >>>>>");
                }
                data.add(user);
            }
        }
    }

    @OnItemClick(values = {R.id.rv_container})
    public void onItemClick(View view, UserInfo userInfo, int position) {
        Toast.makeText(this, "position:" + position + " data:" + userInfo, Toast.LENGTH_SHORT).show();
    }

    @OnItemLongClick(values = {R.id.rv_container})
    public boolean onItemLongClick(View view, UserInfo userInfo, int position) {
        Toast.makeText(this, "long position"+position, Toast.LENGTH_SHORT).show();
        return true;
    }

    @OnRefresh(values = {R.id.refresh})
    public void onRefresh() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                data.clear();
                initData();
                runOnUiThread(() -> {
                    multiAdapter.notifyDataSetChanged();
                    refresh.setRefreshCompleted();
                });
            }
        }.start();
    }

    @OnLoadingMore(values = {R.id.refresh})
    public void onLoadingMore() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                initData();
                runOnUiThread(() -> {
                    multiAdapter.notifyDataSetChanged();
                    refresh.setLoadingMoreCompleted();
                });
            }
        }.start();
    }

}
