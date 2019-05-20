package com.lee.library.adapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author jv.lee
 * description：PageFragment适配器
 */
public class UiPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private Fragment[] fragments;
    private String[] titles;

    public UiPagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public UiPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titles = titles;
    }

    public UiPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList == null ? fragments[position] : fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? fragments.length : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.length > position) {
            return titles[position];
        }
        return "";
    }
}