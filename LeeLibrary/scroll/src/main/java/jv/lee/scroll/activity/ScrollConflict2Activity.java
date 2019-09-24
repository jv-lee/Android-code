package jv.lee.scroll.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import jv.lee.scroll.R;
import jv.lee.scroll.adapter.SimpleAdapter;
import jv.lee.scroll.widget.RecyclerViewSuperEx;

/**
 * @author jv.lee
 * TODO RecyclerView+RecyclerView（纵向同方向） 滑动冲突
 */
public class ScrollConflict2Activity extends AppCompatActivity {

    private SimpleAdapter adapter;
    private List<String> array = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_conflict2);

        RecyclerViewSuperEx rvContainer = findViewById(R.id.rv_container);

        adapter = new SimpleAdapter(this, new ArrayList<>());

        rvContainer.setLayoutManager(new LinearLayoutManager(this));
        rvContainer.setAdapter(adapter);

        notifyDataChange();
    }

    private void notifyDataChange() {
        for (int i = 0; i < 30; i++) {
            array.add("RecyclerViewEx Item -> " + i);
        }
        adapter.updateData(array);
        adapter.notifyDataSetChanged();
    }
}
