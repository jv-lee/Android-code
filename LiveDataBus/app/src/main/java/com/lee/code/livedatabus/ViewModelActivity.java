package com.lee.code.livedatabus;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lee.code.livedatabus.livedata.LiveDataTimerViewModel;

/**
 * @author jv.lee
 */
public class ViewModelActivity extends AppCompatActivity {

    private TextView textView;
    private LiveDataTimerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_model);
        textView = findViewById(R.id.tv_content);

        viewModel = ViewModelProviders.of(this).get(LiveDataTimerViewModel.class);
        viewModel.getmTime().observe(this, s -> {
            textView.setText(s);
        });
    }

    public void stop(View view) {
        viewModel.stopTimer();
    }
}
