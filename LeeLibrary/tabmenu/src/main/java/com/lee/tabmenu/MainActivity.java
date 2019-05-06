package com.lee.tabmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.lee.tabmenu.adapter.TabMenuAdapter;
import com.lee.tabmenu.entity.TagEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRvContainer;
    private TabMenuAdapter mTabMenuAdapter;
    private List<TagEntity> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mRvContainer = findViewById(R.id.rv_container);
        mTabMenuAdapter = new TabMenuAdapter(mData);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mTabMenuAdapter.getItemViewType(position) == TabMenuAdapter.TAB_MENU_ITEM_TYPE_HEADING ||
                        mTabMenuAdapter.getItemViewType(position) == TabMenuAdapter.TAB_MENU_ITEM_TYPE_SUBHEADING) {
                    return 4;
                } else{
                    return 1;
                }
            }
        });
        mRvContainer.setLayoutManager(gridLayoutManager);
        mRvContainer.setAdapter(mTabMenuAdapter);
        ItemTouchCallback itemTouchCallback = new ItemTouchCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRvContainer);
        findViewById(R.id.btn_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData(v);
            }
        });
    }

    private void initData() {
        TagEntity headingTag = new TagEntity();
        headingTag.setViewType(TabMenuAdapter.TAB_MENU_ITEM_TYPE_HEADING);
        mData.add(headingTag);
        for (int i = 0; i < 8; i++) {
            TagEntity entity = new TagEntity();
            entity.setViewType(TabMenuAdapter.TAB_MENU_ITEM_TYPE_ITEM);
            if (i == 0) {
                entity.setResident(true);
                entity.setName("推荐");
            } else if (i == 1) {
                entity.setResident(true);
                entity.setName("军事");
            } else if (i == 2){
                entity.setResident(true);
                entity.setName("娱乐");
            }else{
                entity.setClose(true);
                entity.setName("更多:"+i);
            }
            mData.add(entity);
        }

        TagEntity subheadingTag = new TagEntity();
        subheadingTag.setViewType(TabMenuAdapter.TAB_MENU_ITEM_TYPE_SUBHEADING);
        mData.add(subheadingTag);

        for (int i = 0; i < 8; i++) {
            TagEntity entity = new TagEntity();
            entity.setViewType(TabMenuAdapter.TAB_MENU_ITEM_TYPE_SUB_ITEM);
            if (i == 0) {
                entity.setName("推荐");
                entity.setAdd(true);
            } else if (i == 1) {
                entity.setAdd(true);
                entity.setName("军事");
            } else if(i == 2){
                entity.setAdd(true);
                entity.setName("娱乐");
            }else{
                entity.setAdd(true);
                entity.setName("更多:"+i);
            }
            mData.add(entity);
        }
        mTabMenuAdapter.notifyDataSetChanged();
    }

    public void showData(View view) {
        for (TagEntity mDatum : mData) {
            if (mDatum.getName() != null) {
                Log.i(">>>", mDatum.getName());
            }
        }
        Log.i(">>>", "----------------------- getAdapter().getData()");
        for (TagEntity entity : mTabMenuAdapter.getData()) {
            if (entity.getName() != null) {
                Log.i(">>>", entity.getName());
            }
        }
    }
}
