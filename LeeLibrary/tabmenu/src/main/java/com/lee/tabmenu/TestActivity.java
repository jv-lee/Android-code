package com.lee.tabmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lee.tabmenu.adapter.TabMenuAdapter;
import com.lee.tabmenu.entity.TagEntity;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private TabMenu tabMenu;
    private List<TagEntity> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tabMenu = findViewById(R.id.tab_menu);
        tabMenu.bindView();
        initData();
        tabMenu.bindData(mData);
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
    }
}
