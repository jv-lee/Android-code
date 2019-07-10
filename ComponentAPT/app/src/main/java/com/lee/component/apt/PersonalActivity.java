package com.lee.component.apt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lee.component.annotation.ARouter;

/**
 * @author jv.lee
 */
@ARouter(path = "/app/PersonalActivity")
public class PersonalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
    }

    public void jump(View view) {
        Class<?> targetClass = MainActivity$$ARouter.findTargetClass("/app/MainActivity");
        startActivity(new Intent(this,targetClass));
    }

}
