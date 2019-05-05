package com.lee.music;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lee.music.ui.UIUtils;
import com.lee.music.utils.StatusTool;
import com.lee.music.view.BackgroundAnimationRelativeLayout;
import com.lee.music.view.DiscView;
import com.lee.music.view.MusiclListener;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author jv.lee
 */
public class PlayActivity extends AppCompatActivity {
    private List<Integer> mMusicDatas = new ArrayList<>();
    private BackgroundAnimationRelativeLayout backgroundLayout;
    private DiscView mDisc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusTool.statusBar(this,false);
        UIUtils.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        initView();
        initData();


    }

    private void initView() {
        backgroundLayout = findViewById(R.id.rootLayout);
        mDisc = findViewById(R.id.discview);
        mDisc.setMusiclListener(new MusiclListener() {
            @Override
            public void onMusicPicChanged(int redId) {
                Glide.with(PlayActivity.this)
                        .load(redId)
                        .transform(new BlurTransformation(200, 3))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                backgroundLayout.setForeground(resource);
                            }
                        });
            }
        });
    }

    private void initData() {
        mMusicDatas.add(R.drawable.ic_music3);
        mMusicDatas.add(R.drawable.ic_music1);
        mMusicDatas.add(R.drawable.ic_music2);
        mMusicDatas.add(R.drawable.ic_music3);
        mMusicDatas.add(R.drawable.ic_music1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDisc.setMusicDataList(mMusicDatas);
    }
}
