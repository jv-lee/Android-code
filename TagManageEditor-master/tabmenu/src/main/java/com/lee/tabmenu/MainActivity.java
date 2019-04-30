package com.lee.tabmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
        mTabMenuAdapter = new TabMenuAdapter(this, mData);
        mRvContainer.setLayoutManager(new LinearLayoutManager(this));
        mRvContainer.setAdapter(mTabMenuAdapter);
    }

    private void initData() {
        TagEntity headingTag = new TagEntity();
        headingTag.setViewType(TabMenuAdapter.TAB_MENU_ITEM_TYPE_HEADING);
        mData.add(headingTag);
        for (int i = 0; i < 3;i++) {
            TagEntity entity = new TagEntity();
            entity.setViewType(TabMenuAdapter.TAB_MENU_ITEM_TYPE_ITEM);
            entity.setResident(true);
            if (i == 0) {
                entity.setName("推荐");
            } else if (i == 1) {
                entity.setName("军事");
            } else {
                entity.setName("娱乐");
            }
            mData.add(entity);
        }


        TagEntity subheadingTag = new TagEntity();
        subheadingTag.setViewType(TabMenuAdapter.TAB_MENU_ITEM_TYPE_SUBHEADING);
        mData.add(subheadingTag);

        for (int i = 0; i < 3;i++) {
            TagEntity entity = new TagEntity();
            entity.setViewType(TabMenuAdapter.TAB_MENU_ITEM_TYPE_SUB_ITEM);
            entity.setResident(true);
            if (i == 0) {
                entity.setName("推荐");
            } else if (i == 1) {
                entity.setName("军事");
            } else {
                entity.setName("娱乐");
            }
            mData.add(entity);
        }
        mTabMenuAdapter.notifyDataSetChanged();
    }
}
