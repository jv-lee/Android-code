package code.lee.code.parallax.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;

import code.lee.code.parallax.R;

/**
 * @author jv.lee
 * @date 2019/4/12
 * 获取系统属性Inflater
 */
public class ParallaxLayoutInflater extends LayoutInflater {


    private static final String TAG = ">>>";
    private ParallaxFragment fragment;

    protected ParallaxLayoutInflater(Context context) {
        super(context);
    }

    public ParallaxLayoutInflater(LayoutInflater original, Context newContext) {
        super(original, newContext);
//        setFactory2();
    }

    public ParallaxLayoutInflater(LayoutInflater original, Context newContext,ParallaxFragment fragment) {
        super(original, newContext);
        this.fragment = fragment;
        setFactory2(new ParallaxFactory(this));
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new ParallaxLayoutInflater(this,newContext,fragment);
    }

    class ParallaxFactory implements Factory2 {
        private LayoutInflater inflater;
        /**
         * 系统控件完整路径名前缀
         */
        private final String[] sClassPrefix = {
                "android.widget.",
                "android.view."
        };
        /**
         * 自定义属性
         */
        private int[] attrIds = {
                R.attr.a_in,
                R.attr.a_out,
                R.attr.x_in,
                R.attr.x_out,
                R.attr.y_in,
                R.attr.y_out
        };

        public ParallaxFactory(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        /**
         * @param parent
         * @param name    控件名称 ：ImageView
         * @param context
         * @param attrs
         * @return
         */
        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            Log.i(TAG, "onCreateView");
            View view = null;
            String or = ".";
            //系统视图需要加上完整路径名
            if (name.contains(or)) {
                view = createWidthView(name, context, attrs);
            } else {
                for (String prefix : sClassPrefix) {
                    view = createWidthView(prefix + name, context, attrs);
                    if (view != null) {
                        break;
                    }
                }
            }
            //将 AttributeSet取出自定义属性 封装成javaBean 设置到View
            TypedArray typedArray = context.obtainStyledAttributes(attrs, attrIds);
            if (typedArray != null && typedArray.length() > 0) {
                //设置自定义属性
                ParallaxViewTag tag = new ParallaxViewTag();
                tag.alphain = typedArray.getFloat(0, 0f);
                tag.alphaOut = typedArray.getFloat(1, 0f);
                tag.xIn = typedArray.getFloat(2, 0f);
                tag.xOut = typedArray.getFloat(3, 0f);
                tag.yIn = typedArray.getFloat(4, 0f);
                tag.yOut = typedArray.getFloat(5, 0f);
                if (view != null) {
                    view.setTag(R.id.parallax_view_tag, tag);
                }
                typedArray.recycle();
            }
            //将解析出的view 添加到fragment中
            fragment.getParallaxViews().add(view);
            return view;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            return null;
        }

        private View createWidthView(String className, Context context, AttributeSet attrs) {
            //反射获取
            try {
                Class clazz = Class.forName(className);
                Constructor<View> constructor = clazz.getConstructor(Context.class, AttributeSet.class);
                return constructor.newInstance(context, attrs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
