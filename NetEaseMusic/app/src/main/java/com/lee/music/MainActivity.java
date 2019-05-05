package com.lee.music;

import android.graphics.Color;
import android.graphics.PathMeasure;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lee.music.adapter.MusicAdapter;
import com.lee.music.ui.UIUtils;
import com.lee.music.ui.ViewCalculateUtil;
import com.lee.music.utils.StatusTool;
import com.lee.music.view.NetEaseNestedScrollView;

import java.lang.reflect.Field;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author jv.lee
 */
public class MainActivity extends AppCompatActivity {

    public final static String IMAGE_URL_MEDIUM = "http://p3.music.126.net/iRbTMHGfy-grDtx7o2T_dA==/109951164009413034.jpg?param=800y800";
    int slidingDistance;

    private Toolbar toolbar;
    private ImageView toolbarBg;
    ImageView header_bg;
    RecyclerView music_recycler;
    LinearLayout lv_header_contail;
    ImageView header_music_log;
    ImageView header_image_item;
    NetEaseNestedScrollView myNestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusTool.statusBar(this,false);
        UIUtils.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initView();
        httpPicture();
        notifyData();
        setImgHeaderBg();
    }

    private void setImgHeaderBg() {
        slidingDistance = UIUtils.getInstance().getHeight(850);
        myNestedScrollView.setScrollInterface(new NetEaseNestedScrollView.ScrollInterface() {
            @Override
            public void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.i(">>>", "scroll:"+scrollY);
                scrollChangeHeader(scrollY);
            }
        });
    }

    private void scrollChangeHeader(int scrolledy) {
        if (scrolledy < 0) {
            scrolledy = 0;
        }
        float alpha = Math.abs(scrolledy) * 1.0f / slidingDistance;
        Drawable drawable = toolbarBg.getBackground();
        if (drawable != null) {
            if (scrolledy <= slidingDistance) {
                drawable.mutate().setAlpha((int) (alpha*255));
                toolbarBg.setImageDrawable(drawable);
            }else{
                drawable.mutate().setAlpha(255);
                toolbarBg.setImageDrawable(drawable);
            }
        }else{
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
    }

    private void notifyData() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        music_recycler.setLayoutManager(mLayoutManager);
        // 需加，不然滑动不流畅
        music_recycler.setNestedScrollingEnabled(false);
        music_recycler.setHasFixedSize(false);
        final MusicAdapter adapter = new MusicAdapter(this);
        adapter.notifyDataSetChanged();
        music_recycler.setAdapter(adapter);

    }

    private void httpPicture() {
        Glide.with(this)
                .load(IMAGE_URL_MEDIUM)
                .override(400, 400)
                .into(header_image_item);

        Glide.with(this)
                .load(IMAGE_URL_MEDIUM)
                .error(R.drawable.stackblur_default)
                .placeholder(R.drawable.stackblur_default)
                .transform(new BlurTransformation(250,6))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        lv_header_contail.setBackground(resource);
                    }
                });
            Glide.with(this).load(IMAGE_URL_MEDIUM)
                .error(R.drawable.stackblur_default)
                .transform(new BlurTransformation( 250, 6))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        toolbar.setBackgroundColor(Color.TRANSPARENT);
                        toolbarBg.setBackground(resource);
                        toolbarBg.setImageAlpha(0);
                        toolbarBg.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbarBg = findViewById(R.id.iv_toolbar_bg);
        header_bg = findViewById(R.id.header_bg);
        music_recycler = findViewById(R.id.music_recycler);
        myNestedScrollView = findViewById(R.id.nsv_scrollview);
        header_music_log = findViewById(R.id.header_music_log);
        LinearLayout lv_header_detail = findViewById(R.id.lv_header_detail);
        RelativeLayout rv_header_container = findViewById(R.id.rv_header_container);
        lv_header_contail = findViewById(R.id.lv_header_contail);
        header_image_item = findViewById(R.id.header_image_item);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setTitle("歌单");
        toolbar.setSubtitle("编辑推荐，编辑推荐精选歌曲");
//        toolbar.setBackground(getDrawable(R.drawable.stackblur_default));
        StatusTool.setStatusPadding(this,toolbar);
        ViewCalculateUtil.setViewLayoutParam(toolbar,1080,184,0,0,0,0);
        ViewCalculateUtil.setViewLayoutParam(toolbarBg,1080,(164+UIUtils.getInstance().getSystemBarHeight(this)),0,0,0,0);
        ViewCalculateUtil.setViewLinearLayoutParam(rv_header_container,1080,800,164,0,0,0);
        ViewCalculateUtil.setViewLayoutParam(header_bg, 1080, 850, 0, 0, 0, 0);
        ViewCalculateUtil.setViewLayoutParam(lv_header_detail, 1080, 380, 72, 0, 52, 0);
        ViewCalculateUtil.setViewLinearLayoutParam(header_image_item,380,380);
        ViewCalculateUtil.setViewLayoutParam(header_music_log,60,60,59,0,52,0);
    }

    /**
     * 反射设置toolbar
     */
    private void modifyToolBar(){
        try {
            Field imageButtonField = toolbar.getClass().getDeclaredField("mNavButtonView");
            imageButtonField.setAccessible(true);
            ImageButton imageButton  = (ImageButton) imageButtonField.get(toolbar);
            Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) imageButton.getLayoutParams();
            //适配代码
            layoutParams.topMargin = UIUtils.getInstance().getHeight(20);
            layoutParams.leftMargin = UIUtils.getInstance().getWidth(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
