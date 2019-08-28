package gionee.gnservice.app.tool;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;


/**
 * @author jv.lee
 * @date 2019/5/27.
 * description：
 */
public class GlideTool {
    private volatile static GlideTool mInstance = null;

    private static Context mContext = null;

    private static RequestOptions optionsCommand = null;
    private static DrawableTransitionOptions optionsDrawable = null;

    private GlideTool() {
        initOptions();
    }

    public static GlideTool getInstance(Context context) {
        if (mInstance == null) {
            synchronized (GlideTool.class) {
                if (mInstance == null) {
                    mContext = context;
                    mInstance = new GlideTool();
                }
            }
        }
        return mInstance;
    }

    private void initOptions() {
        //初始化普通加载
        if (optionsCommand == null) {
            optionsCommand = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH);
        }

        //初始化动画加载
        if (optionsDrawable == null) {
            optionsDrawable = new DrawableTransitionOptions();
            optionsDrawable.crossFade();
        }
    }

    public static void loadImage(Object path, ImageView imageView) {
        Glide.with(mContext)
                .load(path)
                .apply(optionsCommand)
                .transition(optionsDrawable)
                .into(imageView);
    }

}
