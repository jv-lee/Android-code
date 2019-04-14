package code.lee.code.parallax.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019/4/12
 */
public class ParallaxPagerAdapter extends FragmentPagerAdapter {

    private List<ParallaxFragment> fragments;

    public ParallaxPagerAdapter(FragmentManager fm, List<ParallaxFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
//        if (fragments != null && fragments.size() > 0) {
//            return fragments.get(i);
//        } else {
//            throw new RuntimeException("ParallaxPagerAdapter.getItem() fragments == null || fragments.size() <= 0");
//        }
    }

    @Override
    public int getCount() {
        return fragments.size();
//        if (fragments != null) {
//            return fragments.size();
//        } else {
//            throw new RuntimeException("ParallaxPagerAdapter.getCount() fragments == null");
//        }
    }
}
