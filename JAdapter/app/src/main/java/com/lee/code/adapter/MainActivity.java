package com.lee.code.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lee.code.adapter.adapter.MultiAdapter;
import com.lee.code.adapter.base.BaseLeeViewActivity;
import com.lee.code.adapter.bean.Userinfo;
import com.lee.library.base.LeeViewAdapter;
import com.lee.library.ioc.annotation.ContentView;
import com.lee.library.listener.ItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 */
@ContentView(R.layout.activity_recyclerview)
public class MainActivity extends BaseLeeViewActivity {

    private List<Userinfo> datas = new ArrayList<>();
    private MultiAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initDatas();
        listener();
    }

    private void initDatas() {
        new Thread(()->{
            if (datas.isEmpty()) {
                for (int i = 0; i < 15; i++) {
                    for (int j = 1; j <= 15; j++) {
                        Userinfo user = new Userinfo();
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
                        }else{
                            user.setType(5);
                            user.setAccount("lee >>>>> 55555 >>>>>");
                        }
                        datas.add(user);
                    }
                }
            }
            notifyAdapterDataSetChanged(datas);
        }).start();
    }

    private void listener() {
        adapter.setItemListener(new ItemListener<Userinfo>() {
            @Override
            public void onItemClick(View view, Userinfo entity, int position) {
                Toast.makeText(context, entity.getAccount(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(View view, Userinfo entity, int position) {
                Toast.makeText(context, entity.getAccount(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void onRefresh() {
        initDatas();
    }

    @Override
    public LeeViewAdapter createRecycleViewAdapter() {
        adapter = new MultiAdapter(null);
        return adapter;
    }
}
