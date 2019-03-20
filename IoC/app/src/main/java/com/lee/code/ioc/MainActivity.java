package com.lee.code.ioc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.code.ioc.base.BaseActivity;
import com.lee.library.ioc.annotation.ContentView;
import com.lee.library.ioc.annotation.InjectView;
import com.lee.library.ioc.annotation.OnClick;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @InjectView(R.id.btn)
    private Button btn;
    @InjectView(R.id.tv)
    private TextView tv;

    @Override
    public void bindData(Bundle savedInstanceState) {
        Toast.makeText(this, btn.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(values = {R.id.btn, R.id.tv})
    public void click(View btn) {
        switch (btn.getId()) {
            case R.id.tv:
                Toast.makeText(this, "textView点击了", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn:
                startActivity(new Intent(MainActivity.this,RecyclerActivity.class));
                break;
        }
    }
}
