package code.lee.code.parallax.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import code.lee.code.parallax.R;

/**
 * @author jv.lee
 * @date 2019/4/11
 * 视察layout
 */
public class ParallaxContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
    private ParallaxPagerAdapter adapter;
    private WeakReference<FragmentActivity> weakActivity;
    private List<ParallaxFragment> fragments;
    private ImageView ivPeople;

    public ParallaxContainer(Context context) {
        this(context, null);
    }

    public ParallaxContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPeople(ImageView ivPeople) {
        this.ivPeople = ivPeople;
    }

    public void setUp(FragmentActivity activity, int... childIds) {
        this.weakActivity = new WeakReference<>(activity);
        //根据布局文件数组 初始化所有的fragment
        fragments = new ArrayList<>();
        for (int childId : childIds) {
            ParallaxFragment fragment = new ParallaxFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("layoutResId", childId);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        //实力化ViewPager
        ViewPager vp = new ViewPager(getContext());
        vp.setId(R.id.parallax_pager);
        vp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        adapter = new ParallaxPagerAdapter(weakActivity.get().getSupportFragmentManager(), fragments);
        //绑定适配器
        vp.setAdapter(adapter);
        addView(vp, 0);
        vp.addOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int containerWidth = getWidth();

        //获取退出页面
        ParallaxFragment outFragment = null;
        try {
            outFragment = fragments.get(position - 1);
        } catch (Exception ignored) {
        }

        //获取进入页面
        ParallaxFragment inFragment = null;
        try {
            inFragment = fragments.get(position);
        } catch (Exception ignored) {
        }

        if (outFragment != null) {
            List<View> outViews = outFragment.getParallaxViews();
            if (outViews != null) {
                for (View view : outViews) {
                    //获取标签，从标签上获取所有动画参数 自定义属性
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                    if (tag == null) {
                        continue;
                    }
                    //宽度距离减去滑动距离
                    view.setTranslationX(containerWidth - positionOffsetPixels * tag.xIn);
                    view.setTranslationY(containerWidth - positionOffsetPixels * tag.yIn);
                }
            }
        }
        if (inFragment != null) {
            List<View> inViews = inFragment.getParallaxViews();
            if (inViews != null) {
                for (View view : inViews) {
                    //获取标签，从标签上获取所有动画参数 自定义属性
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                    if (tag == null) {
                        continue;
                    }
                    //观察推出的fragment中view从原始位置开始向上移动translationY应为负数
                    view.setTranslationY(0 - positionOffsetPixels * tag.yOut);
                    view.setTranslationX(0 - positionOffsetPixels * tag.xOut);

                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        //判断是否为最后一页  是则隐藏人物icon
        if (position == adapter.getCount() - 1) {
            ivPeople.setVisibility(INVISIBLE);
        } else {
            ivPeople.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //滑动时开启动画 不滑动停止动画
        AnimationDrawable animation = (AnimationDrawable) ivPeople.getBackground();
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                animation.start();
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                animation.stop();
                break;
            default:
                break;
        }
    }
}
