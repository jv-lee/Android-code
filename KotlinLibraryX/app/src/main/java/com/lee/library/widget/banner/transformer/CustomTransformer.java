package com.lee.library.widget.banner.transformer;

import android.os.Build;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 *
 * @author Administrator
 * @date 2017/8/15
 */
@androidx.annotation.RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class CustomTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.9F;
    @Override
    public void transformPage(View page, float position) {

        if(position < -1){
            page.setScaleY(MIN_SCALE);
        }else if(position<= 1){
            float scale = Math.max(MIN_SCALE,1 - Math.abs(position));
            page.setScaleY(scale);

        }else{
            page.setScaleY(MIN_SCALE);
        }
    }

}
