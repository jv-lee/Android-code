package com.lee.code.uidraw;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lee.code.uidraw.path.PathView;
import com.lee.code.uidraw.widget.CarView;
import com.lee.code.uidraw.widget.map.MapView;
import com.lee.code.uidraw.widget.split.SplitView;

/**
 * @author jv.lee
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        setContentView(new PathView(this));
//        setContentView(new CarView(this));
//        setContentView(new MapView(this));
        setContentView(new SplitView(this));
    }
}
