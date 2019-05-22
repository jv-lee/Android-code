package com.examples.itemdecoration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.examples.itemdecoration.adapter.SimpleAdapter;
import com.examples.itemdecoration.entity.GroupInfo;
import com.examples.itemdecoration.widget.GroupInfoCallback;
import com.examples.itemdecoration.widget.SectionDecoration;
import com.examples.itemdecoration.widget.StickySectionDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView rvContainer;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvContainer = findViewById(R.id.rv_container);

        initData();
        rvContainer.setLayoutManager(new LinearLayoutManager(this));
        SectionDecoration sectionDecoration = new SectionDecoration(this, new GroupInfoCallback() {
            @Override
            public GroupInfo getGroupInfo(int position) {
                int groupId = position / 5;
                int index = position % 5;
                GroupInfo groupInfo = new GroupInfo(groupId,groupId+"");
                groupInfo.setGroupLength(5);
                groupInfo.setPosition(index);
                return groupInfo;
            }
        });
        StickySectionDecoration stickySectionDecoration = new StickySectionDecoration(this, new GroupInfoCallback() {
            @Override
            public GroupInfo getGroupInfo(int position) {
                int groupId = position / 5;
                int index = position % 5;
                GroupInfo groupInfo = new GroupInfo(groupId,groupId+"");
                groupInfo.setGroupLength(5);
                groupInfo.setPosition(index);
                return groupInfo;
            }
        });
//        rvContainer.addItemDecoration(sectionDecoration);
        rvContainer.addItemDecoration(stickySectionDecoration);
        rvContainer.setAdapter(new SimpleAdapter(list));
    }

    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            list.add("this is item : index -> " + i);
        }
    }
}
