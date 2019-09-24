package jv.lee.scroll.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lee.library.adapter.UiPagerAdapter;

import jv.lee.scroll.R;
import jv.lee.scroll.fragment.SimpleFragment;

/**
 * @author jv.lee
 */
public class ScrollConflict2Activity extends AppCompatActivity {

    private ViewPager container;
    private UiPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_conflict2);
        container = findViewById(R.id.container);

        adapter = new UiPagerAdapter(getSupportFragmentManager(), new Fragment[]{new SimpleFragment(), new SimpleFragment(), new SimpleFragment()});
        container.setAdapter(adapter);
    }
}
