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

    public PhotoView(Context context) {
        super(context);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(new PhotoRender(this));
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
