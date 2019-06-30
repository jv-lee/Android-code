package com.lee.open.photo.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @author jv.lee
 * @date 2019/6/20.
 * description：
 */
public class PhotoView extends GLSurfaceView {

    private PhotoRender mRender;

    public PhotoView(Context context) {
        super(context);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mRender = new PhotoRender(this);
        setRenderer(mRender);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public void enableBeauty(boolean isChecked) {
        mRender.enableBeauty(isChecked);
    }
}
