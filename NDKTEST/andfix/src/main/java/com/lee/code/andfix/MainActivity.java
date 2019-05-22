package com.lee.code.andfix;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_func).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Caclutor caclutor = new Caclutor();
                caclutor.caculator();
                TextView tvText = findViewById(R.id.tv_text);
//        tvText.setText("获取数值：" + num);
            }
        });
    }

    public void func(View view) {

    }

    public void fix(View view) {
        File file = new File(Environment.getExternalStorageDirectory(),"fix.dex");
        Log.i("tag", "fiie:" + file.exists());
        DexManager dexManager = new DexManager(this);
        dexManager.load(file);
    }

}
