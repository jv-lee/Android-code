package com.hclw.bottomnative;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BottomNavGroup bottomNavGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavGroup = findViewById(R.id.bottom_nav_group);
        bottomNavGroup.setItemPositionListener(new BottomNavGroup.ItemPositionListener() {
            @Override
            public void onPosition(RadioButton button, int position) {
                Toast.makeText(MainActivity.this, button.getText()+"：position"+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
