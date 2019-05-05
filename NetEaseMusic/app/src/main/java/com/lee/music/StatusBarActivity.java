package com.lee.music;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lee.music.utils.StatusTool;

public class StatusBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusTool.statusBar(this,false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar);

        findViewById(R.id.iv_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim(v);
            }
        });
    }

    private void startAnim(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationX", 0, 200F);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view, "rotation", 360F);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view, "scaleX", 1, 2);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(view, "scaleY", 1, 2);
        AnimatorSet set = new AnimatorSet();
        set.play(objectAnimator1).after(objectAnimator).with(objectAnimator2).with(objectAnimator3);
        set.setDuration(1000);
        set.start();
    }
}
