package com.hclw.bottomnative;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private BottomNavGroup bottomNavGroup;
    private BottomNavView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavView = findViewById(R.id.bottom_nav_view);
        bottomNavView.initUnReadMessageViews();
        Log.i(">>>", "setDotNotRead");
        bottomNavView.setDotNotRead(0,10);
        bottomNavView.setDotNotRead(0,0);
        bottomNavView.setItemPositionListener(new BottomNavView.ItemPositionListener() {
            @Override
            public void onPosition(MenuItem menuItem, int position) {
                Toast.makeText(MainActivity.this, "position:"+position, Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavGroup = findViewById(R.id.bottom_nav_group);
        bottomNavGroup.setItemPositionListener(new BottomNavGroup.ItemPositionListener() {
            @Override
            public void onPosition(RadioButton button, int position) {
                Toast.makeText(MainActivity.this, button.getText()+"：position"+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
