package com.lee.music;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lee.music.ui.UIUtils;
import com.lee.music.ui.ViewCalculateUtil;
import com.lee.music.view.RippleAnimationView;

/**
 * @author jv.lee
 */
public class AnimatorMusicActivity extends AppCompatActivity {

    RippleAnimationView rippleAnimationView;
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIUtils.getInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anima_music);

        ivImage = findViewById(R.id.iv_image);
        ViewCalculateUtil.setViewLayoutParam(ivImage,300,300,0,0,0,0);
        rippleAnimationView = findViewById(R.id.layout_RippleAnimation);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rippleAnimationView.isAnimationRunning()) {
                    rippleAnimationView.stopRippleAnimation();
                }else{
                    rippleAnimationView.startRippleAnimation();
                }
            }
        });
    }
}
