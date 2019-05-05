package com.lee.music.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lee.music.R;
import com.lee.music.adapter.ViewPagerAdapter;
import com.lee.music.ui.UIUtils;
import com.lee.music.ui.ViewCalculateUtil;
import com.lee.music.utils.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 转盘控件
 */
public class DiscView extends RelativeLayout {

    private List<View> mDiscLayouts = new ArrayList<>();
    private List<Integer> mMusicDatas = new ArrayList<>();
    private List<ObjectAnimator> mDiscAnimators = new ArrayList<>();

    private ImageView musicNeedle;
    private ImageView musicCircle;
    private ViewPager viewPager;
    private ObjectAnimator mNeedleAnimator;
    private ViewPagerAdapter mViewPagerAdapter;
    private MusiclListener musiclListener;

    public DiscView(Context context) {
        super(context);
    }

    public DiscView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public MusiclListener getMusiclListener() {
        return musiclListener;
    }

    public void setMusiclListener(MusiclListener musiclListener) {
        this.musiclListener = musiclListener;
    }

    /**
     * 加载歌曲列表
     *
     * @param musicDataList
     */
    public void setMusicDataList(List<Integer> musicDataList) {
        if (!musicDataList.isEmpty()) {
            mDiscLayouts.clear();
            mMusicDatas.clear();
            mDiscAnimators.clear();
            mMusicDatas.addAll(musicDataList);
            for (Integer resID : mMusicDatas) {
                View centerView = LayoutInflater.from(getContext().getApplicationContext()).inflate(R.layout.layout_disc, viewPager, false);
                ImageView centerImage = centerView.findViewById(R.id.music_img);
                Drawable drawable = BitmapUtil.getMusicItemDrawable(getContext().getApplicationContext(), resID);
                ViewCalculateUtil.setViewLinearLayoutParam(centerImage, 800, 800, (850 - 200) / 2 - 110, 0, 0, 0);
                centerImage.setImageDrawable(drawable);

                mDiscLayouts.add(centerView);

                //指针动画执行 、转盘动画执行
                ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(centerImage, View.ROTATION, 0, 360);
                rotateAnimator.setRepeatCount(ValueAnimator.INFINITE);
                rotateAnimator.setDuration(20 * 1000);
                rotateAnimator.setInterpolator(new LinearInterpolator());
                mDiscAnimators.add(rotateAnimator);
            }
            mViewPagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(1);
            playDiscAnimator(1);

        }
    }

    /**
     * view构建完成后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
        initObjectAnimator();
        mViewPagerAdapter = new ViewPagerAdapter(mDiscLayouts);
        viewPager.setAdapter(mViewPagerAdapter);
    }

    private void initObjectAnimator() {
        mNeedleAnimator = ObjectAnimator.ofFloat(musicNeedle, View.ROTATION,-30, 0);
        mNeedleAnimator.setDuration(500);
        //加速动画
        mNeedleAnimator.setInterpolator(new AccelerateInterpolator());
        mNeedleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                int index = viewPager.getCurrentItem();
                //开启唱盘动画
                playDiscAnimator(index);
            }
        });
    }


    private void initView() {
        //唱针
        musicNeedle = findViewById(R.id.musicNeedle);
        //底盘
        musicCircle = findViewById(R.id.musicCircle);
        viewPager = findViewById(R.id.viewPager);

        //获取宽高
        int musicCircleWidth = UIUtils.getInstance().getWidth(813);
        //获取bitmap 设置宽高
        Bitmap bitmapDisc = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_disc_blackground), musicCircleWidth, musicCircleWidth, false);

        //bitmap转换成圆角drawable图片
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmapDisc);
        //设置背景图片
        musicCircle.setImageDrawable(roundedBitmapDrawable);
        ViewCalculateUtil.setViewLayoutParam(musicCircle, 850, 850, 190, 0, 0, 0);

        //设置指针
        Bitmap originBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_needle);
        Bitmap.createScaledBitmap(originBitmap, UIUtils.getInstance().getWidth(276), UIUtils.getInstance().getWidth(276), false);
        ViewCalculateUtil.setViewLayoutParam(musicNeedle, 276, 413, 43, 0, 500, 0);

        musicNeedle.setPivotX(UIUtils.getInstance().getWidth(43));
        musicNeedle.setPivotY(UIUtils.getInstance().getHeight(43));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentItem = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
            }

            //状态发生改变 设置动画
            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    //什么都没做
                    case ViewPager.SCROLL_STATE_IDLE:

                        break;
                    //开始滑动
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        pauseAnimator();
                        break;
                    //滑动结束
                    case ViewPager.SCROLL_STATE_SETTLING:
                        //播放动画
                        playAnimator();
                        break;
                    default:
                        break;
                }
                if (state != ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                // 当视图在第一个时，将页面号设置为图片的最后一张。
                if (currentItem == 0) {
                    viewPager.setCurrentItem(mViewPagerAdapter.getCount() - 2, false);

                } else if (currentItem == mViewPagerAdapter.getCount() - 1) {
                    // 当视图在最后一个是,将页面号设置为图片的第一张。
                    viewPager.setCurrentItem(1, false);
                }

            }
        });

    }

    private void pauseAnimator() {
        //停止唱盘动画 ，  重置唱针动画
        ObjectAnimator objectAnimator = mDiscAnimators.get(viewPager.getCurrentItem());
        objectAnimator.pause();
        mNeedleAnimator.reverse();
    }

    private void playAnimator() {
        mNeedleAnimator.start();
    }


    private void playDiscAnimator(int index) {
        ObjectAnimator objectAnimator = mDiscAnimators.get(index);
        if (objectAnimator.isPaused()) {
            objectAnimator.resume();
        }else {
            objectAnimator.start();
        }
        if (musiclListener != null) {
            musiclListener.onMusicPicChanged(mMusicDatas.get(viewPager.getCurrentItem()));

        }

    }

}
