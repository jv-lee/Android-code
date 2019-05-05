package com.lee.code.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(new RecyclerView.Adapter() {


            @Override
            public View onCreateViewHolder(int position, View convertView, ViewGroup parent) {
                convertView  = MainActivity.this.getLayoutInflater().inflate(R.layout.item_table1, parent, false);
                TextView textView = convertView.findViewById(R.id.text1);
                textView.setText("第：" + position + "行");
                return convertView;
            }

            @Override
            public View onBinderViewHolder(int position, View convertView, ViewGroup parent) {
                TextView textView = convertView.findViewById(R.id.text1);
                textView.setText("网易课堂 "+position);
                return convertView;
            }

            @Override
            public int getItemViewType(int row) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public int getHeight(int index) {
                return 200;
            }

            @Override
            public int getCount() {
                return 10000;
            }
        });
    }
}
