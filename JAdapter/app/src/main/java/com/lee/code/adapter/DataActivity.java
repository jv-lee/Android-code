package com.lee.code.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lee.code.adapter.adapter.MultiAdapter;
import com.lee.code.adapter.bean.Userinfo;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {

    private List<Userinfo> datas = new ArrayList<>();
    private MultiAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        initDatas();
        RecyclerView recyclerView = findViewById(R.id.rv_container);
        adapter = new MultiAdapter(datas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        }).start();
    }

}
