package com.examples.opengl.filter;

import android.content.Context;

import com.examples.opengl.R;

/**
 * @author jv.lee
 * @date 2019-06-22
 * @description 显示已经渲染好特效的滤镜
 */
public class ScreenFilter extends AbstractFilter {


    public ScreenFilter(Context context) {
        super(context, R.raw.base_vertex, R.raw.base_frag);
    }

    @Override
    protected void initCoordination() {

    }

}
