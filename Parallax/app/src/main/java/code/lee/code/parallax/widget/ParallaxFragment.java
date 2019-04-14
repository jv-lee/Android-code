package code.lee.code.parallax.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jv.lee
 * @date 2019/4/12
 */
public class ParallaxFragment extends Fragment {

    /**
     * 加载布局 了解有哪些需要差异化规定
     * 此Fragment上所有的需要实现视差动画的视图view
     */
    private List<View> parallaxViews = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        assert arguments != null;
        int layoutResId = arguments.getInt("layoutResId");
        ParallaxLayoutInflater in = new ParallaxLayoutInflater(inflater, getActivity(), this);
        return in.inflate(layoutResId, null);
    }

    public List<View> getParallaxViews() {
        return parallaxViews;
    }

}
