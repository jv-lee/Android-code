package com.lee.open.photo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.lee.open.photo.widget.PhotoView;

/**
 * @author jv.lee
 */
public class MainActivity extends Activity {

    private PhotoView photoView;
    private Switch switchBeauty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photoView = findViewById(R.id.photo_view);
        switchBeauty = findViewById(R.id.switch_beauty);

        switchBeauty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                photoView.enableBeauty(isChecked);
            }
        });
    }

}
