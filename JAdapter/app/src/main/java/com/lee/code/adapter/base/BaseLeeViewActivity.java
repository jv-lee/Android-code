package com.lee.code.adapter.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lee.code.adapter.R;
import com.lee.library.LeeViewHelper;
import com.lee.library.SwipeRefreshHelper;
import com.lee.library.ioc.InjectManager;
import com.lee.library.listener.LeeViewCreate;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019/3/29
 */
public abstract class BaseLeeViewActivity extends AppCompatActivity
        implements LeeViewCreate,SwipeRefreshHelper.SwipeRefreshListener {
    protected LeeViewHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectManager.inject(this);
        helper = new LeeViewHelper.Builder(this,this).build();
    }

    @Override
    public SwipeRefreshLayout createSwipeRefresh() {
        return findViewById(R.id.refresh);
    }

    @Override
    public int[] colorRes() {
        return new int[0];
    }

    @Override
    public RecyclerView createRecyclerView() {
        return findViewById(R.id.rv_container);
    }


    @Override
    public RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public int startPageNumber() {
        return 1;
    }

    @Override
    public boolean isSupportPaging() {
        return false;
    }

    protected void notifyAdapterDataSetChanged(List datas) {
        helper.notifyAdapterDataSetChanged(datas);
    }
}
